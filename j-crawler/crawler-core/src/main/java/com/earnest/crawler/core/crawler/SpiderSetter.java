package com.earnest.crawler.core.crawler;


public interface SpiderSetter extends Spider {
    <T> void setCrawler(Crawler<T> crawler);

    void setThread(int threadNumber);

}
