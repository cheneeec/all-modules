package com.earnest.crawler;


import com.earnest.crawler.downloader.Downloader;
import com.earnest.crawler.exception.TakeTimeoutException;
import com.earnest.crawler.extractor.HttpRequestExtractor;
import com.earnest.crawler.pipeline.Pipeline;
import com.earnest.crawler.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.Assert;
import org.springframework.util.CustomizableThreadCreator;

import java.util.concurrent.*;

@Slf4j
public class AsyncSpider extends SyncSpider {

    private final ExecutorService threadPool;
    private final int threadNumber;


    public AsyncSpider(Downloader downloader,
                       Scheduler scheduler,
                       HttpRequestExtractor httpRequestExtractor,
                       Pipeline pipeline,
                       ExecutorService threadPool,
                       int threadNumber) {
        super(downloader, scheduler, httpRequestExtractor, pipeline);
        Assert.isTrue(threadNumber > 0, "threadNumber <1");
        Assert.notNull(threadPool, "threadPool is required");
        this.threadPool = threadPool;
        this.threadNumber = threadNumber;
        if (threadPool instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) threadPool).setThreadFactory(new SpiderThreadFactory());
        }

    }

    public AsyncSpider(Downloader downloader,
                       Scheduler scheduler,
                       HttpRequestExtractor httpRequestExtractor,
                       Pipeline pipeline,
                       ThreadPoolExecutor threadPool) {
        this(downloader, scheduler, httpRequestExtractor, pipeline, threadPool, threadPool.getMaximumPoolSize());

    }


    @Override
    public void start() {

        CountDownLatch completed = new CountDownLatch(threadNumber);
        //进行下载
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(() -> {
                while (true) {
                    try {
                        HttpUriRequest httpUriRequest = scheduler.take();
                        if (httpUriRequest == null) {
                            break;
                        }
                        handleStringResponseResult(downloader.download(httpUriRequest));
                    } catch (TakeTimeoutException e) {
                        break;
                    }
                }
                //完成标识
                completed.countDown();
            });
        }

        try {
            //等待任务完成
            completed.await();
            log.info("download completed, exit...");
            afterCompleted();
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for the task to complete,error:{}", e.getMessage());
        }


    }


    @Override
    public void stop() {
        threadPool.shutdown();
    }


    @Override
    public void close() {
        if (!threadPool.isShutdown()) {
            threadPool.shutdown();
        }
        super.close();
    }


    /**
     * 爬虫的线程工厂
     */
    private class SpiderThreadFactory extends CustomizableThreadCreator implements ThreadFactory {
        final static String DEFAULT_THREAD_GROUP_NAME = "spider";
        final static String DEFAULT_THREAD__NAME_PREFIX = "crawler";

        public SpiderThreadFactory() {
            this(DEFAULT_THREAD__NAME_PREFIX);
        }

        public SpiderThreadFactory(String threadNamePrefix) {
            super(threadNamePrefix);
            setThreadGroupName(DEFAULT_THREAD_GROUP_NAME);
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return createThread(runnable);
        }
    }

}
