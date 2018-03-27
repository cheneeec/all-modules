package com.earnest.crawler.core.crawler;

public interface Spider {
    <T> void setCrawler(Crawler<T> crawler);

    void setThread(int threadNumber);

    void start();

    void shutdown();
}
