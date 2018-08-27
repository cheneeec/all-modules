package com.earnest.crawler.core.scheduler;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedArrayScheduler implements Scheduler {

    private final HttpUriRequest[] tasks;

    private AtomicInteger index = new AtomicInteger();


    public FixedArrayScheduler(int initialCapacity) {
        tasks = new HttpUriRequest[initialCapacity];
    }


    @Override
    public boolean isEmpty() {
        return index.get() < 0;
    }

    @Override
    public HttpUriRequest take() {
        if (index.get() < 1) return null;
        int i = index.decrementAndGet();
        HttpUriRequest task = tasks[i];
        tasks[i] = null;
        return task;
    }

    @Override
    public boolean put(HttpUriRequest httpUriRequest) {
        if (httpUriRequest == null) return true;
        int i = index.getAndIncrement();
        tasks[i] = httpUriRequest;
        return true;
    }

    @Override
    public void putAll(Collection<HttpUriRequest> httpUriRequests) {
        if (CollectionUtils.isEmpty(httpUriRequests)) return;
        httpUriRequests.forEach(this::put);
    }


}
