package com.earnest.crawler.core.scheduler;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 固定请求的调度器。
 */
public class FixedScheduler implements Scheduler {

    private final Set<HttpUriRequest> taskSet;

    public FixedScheduler(int initialCapacity) {
        this.taskSet = Collections.newSetFromMap(new ConcurrentHashMap<>(initialCapacity));
    }

    public FixedScheduler() {
        this(16);
    }

    @Override
    public boolean isEmpty() {
        return taskSet.isEmpty();
    }

    @Override
    public HttpUriRequest take() {
        Iterator<HttpUriRequest> iterator = taskSet.iterator();
        if (iterator.hasNext()) {
            HttpUriRequest next = iterator.next();
            iterator.remove();
            return next;
        }
        return null;
    }

    @Override
    public boolean put(HttpUriRequest httpUriRequest) {
        if (httpUriRequest == null) return true;
        return taskSet.add(httpUriRequest);
    }

    @Override
    public void putAll(Collection<HttpUriRequest> httpUriRequests) {
        if (CollectionUtils.isEmpty(httpUriRequests)) return;
        taskSet.addAll(httpUriRequests);

    }
}
