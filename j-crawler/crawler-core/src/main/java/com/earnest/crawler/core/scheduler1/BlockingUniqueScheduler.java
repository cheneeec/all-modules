package com.earnest.crawler.core.scheduler1;

import com.earnest.crawler.core.exception.TakeTimeoutException;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.scheduler.AbstractDownloadListenerScheduler;
import com.earnest.crawler.core.scheduler.BlockingScheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
public class BlockingUniqueScheduler implements Scheduler {
    private BlockingQueue<HttpUriRequest> queue = new ArrayBlockingQueue<>(1000);

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public HttpUriRequest take() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean put(HttpUriRequest httpUriRequest) {
        try {
            queue.put(httpUriRequest);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
