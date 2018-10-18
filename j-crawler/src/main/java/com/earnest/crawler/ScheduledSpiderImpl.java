package com.earnest.crawler;


import com.earnest.crawler.downloader.Downloader;
import com.earnest.crawler.extractor.HttpRequestExtractor;
import com.earnest.crawler.pipeline.Pipeline;
import com.earnest.crawler.scheduler.Scheduler;

import java.util.concurrent.ExecutorService;

public class ScheduledSpiderImpl extends AsyncSpider implements ScheduledSpider {


    public ScheduledSpiderImpl(Downloader downloader, Scheduler scheduler, HttpRequestExtractor httpRequestExtractor, Pipeline pipeline, ExecutorService threadPool, int threadNumber) {
        super(downloader, scheduler, httpRequestExtractor, pipeline, threadPool, threadNumber);
    }
}
