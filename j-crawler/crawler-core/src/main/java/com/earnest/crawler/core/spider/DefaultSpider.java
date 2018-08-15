package com.earnest.crawler.core.spider;


import com.earnest.crawler.core.StringResponseResult;
import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.downloader1.Downloader;
import com.earnest.crawler.core.exception.TakeTimeoutException;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler1.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.Assert;
import org.springframework.util.CustomizableThreadCreator;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
@Slf4j
public class DefaultSpider implements Spider {

    private final Downloader downloader;
    private final Scheduler scheduler;
    private final HttpRequestExtractor httpRequestExtractor;
    private final Pipeline pipeline;
    private final ThreadPoolExecutor threadPool;

    public DefaultSpider(Downloader downloader, Scheduler scheduler, HttpRequestExtractor httpRequestExtractor, Pipeline pipeline, Integer threadNumber) {
        Assert.notNull(downloader, "downloader is null");
        Assert.notNull(scheduler, "scheduler is null");
        Assert.notNull(pipeline, "pipeline is null");
        this.downloader = downloader;
        this.scheduler = scheduler;
        this.httpRequestExtractor = httpRequestExtractor;
        this.pipeline = pipeline;
        this.threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNumber, new SpiderThreadFactory());

    }


    @Override
    public void start() {
        int threadNumber = threadPool.getMaximumPoolSize();
        BlockingQueue<StringResponseResult> responseResultsQueue = new ArrayBlockingQueue<>(1000);


        //进行下载
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(() -> {
                HttpUriRequest httpUriRequest = null;
                while (true) {
                    try {
                        httpUriRequest = scheduler.take();
                    } catch (TakeTimeoutException e) {
//                        e.printStackTrace();
                        System.out.println("当前活动线程数"+threadPool.getActiveCount());
                        break;
                    }

                    if (httpUriRequest != null) {
                        StringResponseResult responseResult = downloader.download(httpUriRequest);
                        try {
                            //一般来说不会放满
                            responseResultsQueue.put(responseResult);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("当前活动线程数"+threadPool.getActiveCount());
                System.out.println("d当前完成任务的数量"+  threadPool.getCompletedTaskCount());

            });
        }
        //对结果进行处理
        handleResult(responseResultsQueue);

    }

    /**
     * 对结果返回结果进行处理。
     *
     * @param responseResultsQueue
     */
    private void handleResult(BlockingQueue<StringResponseResult> responseResultsQueue) {
        while (true) {
            try {
                //获取下载的结果
                StringResponseResult stringResponseResult = responseResultsQueue.take();
                //对下载的结果进行提取
                Set<HttpUriRequest> httpUriRequests = httpRequestExtractor.extract(stringResponseResult);
                //将结果放入
                scheduler.putAll(httpUriRequests);
                //将结果进行处理
                pipeline.pipe(stringResponseResult);

                if (threadPool.getActiveCount() == 0) {
                    log.info("download completed, exit...");
                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
                //线程被打断发出的异常
            }
        }

    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void close() throws IOException {

    }


    static class SpiderThreadFactory extends CustomizableThreadCreator implements ThreadFactory {
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
