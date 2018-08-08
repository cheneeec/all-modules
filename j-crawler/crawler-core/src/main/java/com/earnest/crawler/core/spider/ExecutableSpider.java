package com.earnest.crawler.core.spider;

import com.earnest.crawler.core.crawler.Spider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutableSpider implements Spider {

    @Getter
    private final Crawler crawler;
    private final ExecutorService threadPool;
    @Setter
    private int threadNumber;

    private String name;

    private boolean closed;

    private volatile boolean running;

    public ExecutableSpider(Crawler crawler, ExecutorService threadPool, int threadNumber) {
        Assert.notNull(crawler, "crawler is null");
        Assert.isTrue(threadNumber > 0, "threadNumber must be bigger than 0");
        this.crawler = crawler;
        this.threadPool = threadPool;
        this.threadNumber = threadNumber;
    }

    public ExecutableSpider(Crawler crawler, int threadNumber) {
        this(crawler, Executors.newFixedThreadPool(threadNumber), threadNumber);
    }

    public ExecutableSpider(Crawler crawler) {
        this(crawler, 5);
    }


    @Override
    public void start() {
        this.running = true;
        crawler.setRunning(running);
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(new Thread(crawler, name + "-crawler-" + i));
        }

    }

    @Override
    public void shutdown() {
        this.running = false;
        threadPool.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return threadPool.shutdownNow();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        crawler.close();
    }
}
