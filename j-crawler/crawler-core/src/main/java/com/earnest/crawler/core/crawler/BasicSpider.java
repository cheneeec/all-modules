package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.crawler.listener.StopListener;
import com.earnest.crawler.core.event.CrawlerStopEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static java.util.Objects.nonNull;

@Slf4j
public class BasicSpider implements SpiderSetter, StopListener {

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

        crawler.addStopListener(this);
        threadPool = Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(new Thread(crawler, "crawler" + i));
        }

    }

    @Override
    public void shutdown() {
//        crawler.destroy();
        threadPool.shutdown();
    }

    @Override
    public List<Runnable> stopNow() {
        crawler.destroy();
        return threadPool.shutdownNow();
    }


    @Override
    public void onStop(CrawlerStopEvent stop) {
        log.info("the spider is stopping at {}", stop.getTime());
        shutdown();
    }
}
