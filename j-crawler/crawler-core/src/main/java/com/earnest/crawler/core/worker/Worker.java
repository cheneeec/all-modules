package com.earnest.crawler.core.worker;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@Slf4j
@Getter
@Setter(AccessLevel.PROTECTED)
class Worker<T> implements IWorker, Runnable {

    private volatile boolean pause = false;

    private Scheduler scheduler;
    private Pipeline<T> pipeline;
    private HttpResponseHandler responseHandler;
    private Downloader downloader;

    private Set<Consumer<T>> persistenceConsumers;

    private static final String NAME = Thread.currentThread().getName();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Condition condition = new ReentrantLock().newCondition();

    @Override
    public void pause() {
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        try {
            writeLock.lock();
            pause = true;
            log.info("Thread name:{},has been suspended at {}", NAME, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Thread name:{},An error:[{}] occurred while pausing", e.getMessage());
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public boolean isPause() {
        ReentrantReadWriteLock.ReadLock readLock = this.lock.readLock();

        try {
            readLock.lock();
            return pause;
        } finally {
            readLock.unlock();
        }

    }

    @Override
    public void restart() {
        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

        try {
            writeLock.lock();
            pause = false;
            condition.signalAll();
            log.info("Thread:{},restart running", NAME);
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void run() {
        log.info("{} is running", NAME);
        while (!scheduler.isEmpty()) {
            //暂停
            try {
                if (pause) {
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //1. 获取连接
            HttpRequest httpRequest = this.scheduler.poll();
            if (nonNull(httpRequest)) {
                HttpResponse httpResponse = downloader.download(httpRequest);
                //2. 处理HttpResponse并且获取新的连接
                Set<HttpRequest> newHttpRequests = responseHandler.handle(httpResponse);
                log.info("{} get a new url:{}", NAME, JSONObject.toJSONString(newHttpRequests));
                //3. 将新的连接放入
                scheduler.addAll(newHttpRequests);
                //4. 将httpResponse转化成实体类
                T pipeResult = pipeline.pipe(httpResponse);
                //5. 将结果进行消化
                persistenceConsumers.parallelStream().forEach(a -> a.accept(pipeResult));

            }
        }
    }


}
