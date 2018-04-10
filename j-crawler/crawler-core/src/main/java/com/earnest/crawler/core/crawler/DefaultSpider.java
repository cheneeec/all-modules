package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.crawler.listener.StopListener;
import com.earnest.crawler.core.event.CrawlerStopEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static java.util.Objects.nonNull;

@Slf4j
public class DefaultSpider implements SpiderSetter, StopListener {

    private Crawler<?> crawler;
    private int threadNumber = 1;
    private ExecutorService threadPool;
    private boolean running = true;
    private boolean closed;

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
        Assert.state(!closed, "spider has been closed");
        Assert.state(!running, "spider has been running");
        crawler.addStopListener(this);
        threadPool = Executors.newFixedThreadPool(threadNumber);
        for (int i = 0; i < threadNumber; i++) {
            threadPool.execute(new Thread(crawler));
        }
    }

    @Override
    public void shutdown() {
        threadPool.shutdown();
        while (true) {
            if (crawler.getScheduler().isEmpty()) {
                Collection<Thread> threads = ThreadUtils.findThreads(t -> StringUtils.startsWith(t.getName(), "pool-1-thread"));
                if (!CollectionUtils.isEmpty(threads)) {
                    threads.stream().filter(Thread::isAlive).forEach(Thread::interrupt);
                }
                running = false;
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
    public void onStop(CrawlerStopEvent stop) {
        log.info("the spider is stopping at {}", stop.getTime());
        shutdown();
    }

    @Override
    public void close() throws IOException {
        crawler.close();
        closed = true;
    }

}
