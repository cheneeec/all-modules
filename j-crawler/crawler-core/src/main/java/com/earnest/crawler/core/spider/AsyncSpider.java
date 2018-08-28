package com.earnest.crawler.core.spider;


import com.earnest.crawler.core.StringResponseResult;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.exception.TakeTimeoutException;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.CustomizableThreadCreator;

import java.util.concurrent.*;

@Slf4j
public class AsyncSpider extends SyncSpider {

    private final ThreadPoolExecutor threadPool;


    public AsyncSpider(Downloader downloader, Scheduler scheduler, HttpRequestExtractor httpRequestExtractor, Pipeline pipeline, Integer threadNumber) {
        super(downloader, scheduler, httpRequestExtractor, pipeline);
        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNumber + 1, new SpiderThreadFactory());

    }


    @Override
    public void start() {

        int threadNumber = threadPool.getMaximumPoolSize();

        final BlockingQueue<StringResponseResult> responseResultsQueue = new ArrayBlockingQueue<>(100);


        final Thread resultHandleThread = createResultHandleThread(responseResultsQueue);


        //进行下载
        for (int i = 0; i < threadNumber - 1; i++) {
            threadPool.execute(() -> {
                HttpUriRequest httpUriRequest;
                while (true) {
                    try {
                        httpUriRequest = scheduler.take();
                    } catch (TakeTimeoutException e) {
                        break;
                    }

                    if (httpUriRequest != null) {
                        StringResponseResult responseResult = downloader.download(httpUriRequest);
                        //放入请求
                        try {
                            responseResultsQueue.put(responseResult);
                        } catch (InterruptedException e) {
                            log.error("Interrupted when placing {}", responseResult);
                        }
                    } else
                        break; //拿到null时
                }
                if (threadPool.getActiveCount() == 2) {//当活动线程只剩下最后一个
                    while (!responseResultsQueue.isEmpty()) {
                        try {
                            //等待响应结果处理完成
                            TimeUnit.MILLISECONDS.sleep(50);
                        } catch (InterruptedException e) {
                            log.error("Interrupted while processing the http response");
                            resultHandleThread.interrupt();
                        }
                    }
                    resultHandleThread.interrupt();
                }

            });
        }

        //对结果进行处理
        threadPool.execute(resultHandleThread);
    }

    private Thread createResultHandleThread(BlockingQueue<StringResponseResult> responseResultsQueue) {
        return new Thread(() -> {
            while (true) {
                try {
                    //当有活动的线程才去取值
                    if (threadPool.getActiveCount() > 1) {
                        stringResponseResultHandle(responseResultsQueue.take());
                    } else
                        break;
                } catch (InterruptedException e) {//在线程结果处理完成后发生
                    break;
                }
            }
            log.info("download completed, exit...");
        });
    }


    @Override
    public void stop() {
        threadPool.shutdown();
    }


    @Override
    public boolean isRunning() {
        return threadPool.getActiveCount() != 0;
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
