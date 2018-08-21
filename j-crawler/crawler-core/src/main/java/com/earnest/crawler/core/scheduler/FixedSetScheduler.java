package com.earnest.crawler.core.scheduler;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * 固定请求的调度器。线程不安全。
 */
public class FixedSetScheduler implements Scheduler {

    private final Set<HttpUriRequest> taskSet;


    public FixedSetScheduler(int initialCapacity) {
        taskSet = new LinkedHashSet<>(initialCapacity);
    }

    public FixedSetScheduler() {
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
