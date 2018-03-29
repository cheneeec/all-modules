package com.earnest.crawler.core.crawler;

import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        Assert.state(Objects.nonNull(crawler), "crawler is neo set");

        threadPool = Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(crawler);
        }

    }

    @Override
    public void shutdown() {
        threadPool.shutdownNow();
    }


}
