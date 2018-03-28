package com.earnest.crawler.core.crawler;

public interface Spider extends Switcher {
    <T> void setCrawler(Crawler<T> crawler);

    void setThread(int threadNumber);


}
