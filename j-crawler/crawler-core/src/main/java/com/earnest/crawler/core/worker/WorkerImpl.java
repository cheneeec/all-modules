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
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Slf4j
@Getter
@Setter(AccessLevel.PROTECTED)
class WorkerImpl<T> implements Worker, Runnable {

    private volatile boolean pause = false;
    private CountDownLatch pauseCountDown;

    private Scheduler scheduler;
    private Pipeline<T> pipeline;
    private HttpResponseHandler responseHandler;
    private Downloader downloader;


    private Consumer<T> persistenceConsumer;

    @Override
    public void pause() {
        this.pauseCountDown = new CountDownLatch(1);
        this.pause = true;
        log.info("Thread name:{},has been suspended at {}", Thread.currentThread().getName(), LocalDateTime.now().toString());
    }

    @Override
    public boolean isPause() {
        return pause;
    }

    @Override
    public void restart() {
        this.pauseCountDown.countDown();
        this.pause = false;
        log.info("Thread:{},restart the work", Thread.currentThread().getName());
    }

    @Override
    public void run() {
        while (!scheduler.isEmpty()) {
            //暂停
            if (pause) {
                try {
                    this.pauseCountDown.await();
                } catch (InterruptedException e) {
                    log.error("Thread name:{},An error:[{}] occurred while suspending at {}", Thread.currentThread().getName(), e.getMessage(), LocalDateTime.now().toString());
                }
            }
            //1. 获取连接
            HttpRequest httpRequest = this.scheduler.poll();
            if (Objects.nonNull(httpRequest)) {
                HttpResponse httpResponse = downloader.download(httpRequest);
                //2. 处理HttpResponse并且获取新的连接
                List<HttpRequest> newHttpRequests = responseHandler.handle(httpResponse);
                //3. 将新的连接放入
                scheduler.addAll(newHttpRequests);
                //4. 将httpResponse转化成实体类
                pipeline.pipe(httpResponse).forEach(persistenceConsumer);
                //5. 将结果消化
            }

        }

    }


}
