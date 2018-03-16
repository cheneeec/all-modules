package com.earnest.crawler.core.worker;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class CrawlerWorker implements Worker {

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    private volatile boolean pause = false;


    @Override
    public void pause() {
        try {
            countDownLatch.await();
            this.pause = true;
            log.info("Thread name:{},has been suspended at {}", Thread.currentThread().getName(), LocalDateTime.now().toString());
        } catch (InterruptedException e) {
            log.error("Thread name:{},An error:[{}] occurred while suspending at {}", Thread.currentThread().getName(), e.getMessage(), LocalDateTime.now().toString());
        }
    }

    @Override
    public boolean isPause() {
        return pause;
    }

    @Override
    public void restart() {
        countDownLatch.countDown();
        this.pause = false;
        log.info("Thread:{},restart the work", Thread.currentThread().getName());
    }

    @Override
    public void run() {

    }


}
