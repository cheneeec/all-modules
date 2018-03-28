package com.earnest.crawler.core.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BasicSpider implements Spider {

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
        threadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(crawler);
        }
    }

    @Override
    public void shutdown() {
        threadPool.shutdownNow();
    }


}
