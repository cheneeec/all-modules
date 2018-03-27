package com.earnest.crawler.core.crawler;

public class BasicSpider implements Spider {

    private Crawler<?> crawler;
    private int threadNumber = 1;

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

    }

    @Override
    public void shutdown() {

    }
}
