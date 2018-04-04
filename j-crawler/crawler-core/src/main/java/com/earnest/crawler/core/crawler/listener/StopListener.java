package com.earnest.crawler.core.crawler.listener;

import com.earnest.crawler.core.event.CrawlerStopEvent;

import java.util.EventListener;

@FunctionalInterface
public interface StopListener extends EventListener {
    void onStop(CrawlerStopEvent stopEvent);
}
