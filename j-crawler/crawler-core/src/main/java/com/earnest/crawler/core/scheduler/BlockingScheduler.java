package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.exception.TakeTimeoutException;
import com.earnest.crawler.core.request.HttpRequest;

import java.util.concurrent.TimeUnit;

public interface BlockingScheduler extends Scheduler {
    HttpRequest take(long time, TimeUnit unit) throws InterruptedException,TakeTimeoutException;

}
