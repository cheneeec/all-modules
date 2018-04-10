package com.earnest.crawler.core.crawler;


import com.earnest.crawler.core.MultiThreadBean;

public interface BasicSpider extends Spider, MultiThreadBean {
    <T> void setCrawler(Crawler<T> crawler);
}
