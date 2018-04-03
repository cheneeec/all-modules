package com.earnest.crawler.core.crawler;

import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;

public class BasicSpider implements SpiderSetter {

    private Crawler<?> crawler;
    private int threadNumber = 1;
    private ExecutorService threadPool;

    @Override
    public <T> void setCrawler(Crawler<T> crawler) {
        this.crawler = crawler;
    }

    @Override
    public void setThread(int threadNumber) {
        this.threadNumber = threadNumber;
    }


    @Override
    public void start() {
        Assert.state(nonNull(crawler), "crawler is not set");

        threadPool = Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(new Thread(crawler, "crawler" + i));
        }
    }

    @Override
    public void shutdown() {
        crawler.destroy();
        threadPool.shutdownNow();
    }


}
