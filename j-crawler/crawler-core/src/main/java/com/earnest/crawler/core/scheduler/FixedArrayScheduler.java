package com.earnest.crawler.core.scheduler;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedArrayScheduler implements Scheduler {

    private final List<HttpUriRequest> tasks;

    private AtomicInteger index = new AtomicInteger();


    public FixedArrayScheduler(int initialCapacity) {
        tasks = new ArrayList<>(initialCapacity);
    }

    public FixedArrayScheduler() {
        this(30);
    }

    @Override
    public boolean isEmpty() {
        return index.get() < 0;
    }

    @Override
    public HttpUriRequest take() {
        if (index.get() < 1) return null;
        int i = index.decrementAndGet();
        return tasks.remove(i);
    }

    @Override
    public boolean put(HttpUriRequest httpUriRequest) {
        if (httpUriRequest == null) return true;
        int i = index.getAndIncrement();
        tasks.add(i, httpUriRequest);
        return true;
    }

    @Override
    public void putAll(Collection<HttpUriRequest> httpUriRequests) {
        if (CollectionUtils.isEmpty(httpUriRequests)) return;
        httpUriRequests.forEach(this::put);
    }


}
