package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.crawler.listener.StopListener;
import com.earnest.crawler.core.event.CrawlerStopEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.*;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static java.util.Objects.nonNull;

@Slf4j
public class DefaultSpider implements BasicSpider, StopListener {

    private Crawler<?> crawler;
    private int threadNumber = 1;
    private ExecutorService threadPool;
    private volatile boolean running;
    private volatile boolean closed;
    private String name;

    private Map<Integer, Thread> crawlerMap;

    @Override
    public <T> void setCrawler(Crawler<T> crawler) {
        this.crawler = crawler;
    }

    @Override
    public Crawler getCrawler() {
        return crawler;
    }

    @Override
    public void setThread(int threadNumber) {
        this.threadNumber = threadNumber;
    }


    @Override
    public void start() {
        Assert.state(nonNull(crawler), "crawler is not set");
        Assert.state(!closed, "spider has been closed");
        Assert.state(!running, "spider has been running");
        crawler.addStopListener(this);
        threadPool = Executors.newFixedThreadPool(threadNumber);
        crawlerMap = new HashMap<>(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            Thread crawlerThread = new Thread(crawler);
            crawlerMap.put(i, crawlerThread);
            threadPool.execute(crawlerThread);
        }
        running = true;
    }

    @Override
    public void shutdown() {
        Assert.state(running, String.format("spider:[%s] has not been running yet", name));
        threadPool.shutdown();
        while (true) {
            if (crawler.getScheduler().isEmpty()) {
                crawlerMap.values().forEach(Thread::interrupt);
                running = false;
                log.info("The spider:[{}] has stopped at {}", name, LocalDateTime.now());
                break;
            }
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        running = false;
        return threadPool.shutdownNow();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public String getName() {
        if (StringUtils.isBlank(name)) {
            name = RandomStringUtils.random(6);
        }
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void onStop(CrawlerStopEvent stop) {
        log.info("the spider:[{}] is stopping at {}", name, stop.getTime());
        shutdown();
    }

    @Override
    public void close() throws IOException {
        crawler.close();
        closed = true;
    }
}
