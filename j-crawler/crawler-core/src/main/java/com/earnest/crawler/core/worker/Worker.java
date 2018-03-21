package com.earnest.crawler.core.worker;

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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Slf4j
@Getter
@Setter(AccessLevel.PROTECTED)
class Worker<T> implements IWorker, Runnable {

    private volatile boolean pause = false;
    private CountDownLatch pauseCountDown;

    private Scheduler scheduler;
    private Pipeline<T> pipeline;
    private HttpResponseHandler responseHandler;
    private Downloader downloader;

    private Set<Consumer<T>> persistenceConsumers;

    private static final String NAME = Thread.currentThread().getName();

    @Override
    public void pause() {
        pauseCountDown = new CountDownLatch(1);
        pause = true;
        log.info("Thread name:{},has been suspended at {}", NAME, LocalDateTime.now().toString());
    }

    @Override
    public boolean isPause() {
        return pause;
    }

    @Override
    public void restart() {
        pauseCountDown.countDown();
        pause = false;
        log.info("Thread:{},restart running", NAME);
    }

    @Override
    public void run() {
        log.info("{} is running", NAME);
        while (!scheduler.isEmpty()) {
            //暂停
            if (pause) {
                try {
                    pauseCountDown.await();
                } catch (InterruptedException e) {
                    log.error("Thread name:{},An error:[{}] occurred while suspending at {}", NAME, e.getMessage(), LocalDateTime.now().toString());
                }
            }
            //1. 获取连接
            HttpRequest httpRequest = this.scheduler.poll();
            if (Objects.nonNull(httpRequest)) {
                HttpResponse httpResponse = downloader.download(httpRequest);
                //2. 处理HttpResponse并且获取新的连接
                List<HttpRequest> newHttpRequests = responseHandler.handle(httpResponse);
                log.info("{} get a new url:{}", NAME, newHttpRequests);
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
