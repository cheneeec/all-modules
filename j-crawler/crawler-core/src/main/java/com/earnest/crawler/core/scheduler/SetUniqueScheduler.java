package com.earnest.crawler.core.scheduler;


import com.earnest.crawler.core.request.HttpRequest;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SetUniqueScheduler extends AbstractDownloadListenerScheduler {
    private final Set<HttpRequest> taskSet;
    private final Set<String> historyTaskSet;

    public SetUniqueScheduler(int initialCapacity) {
        super(Collections.newSetFromMap(new ConcurrentHashMap<>(initialCapacity * 10)), Collections.newSetFromMap(new ConcurrentHashMap<>(initialCapacity / 10)));
        taskSet = Collections.newSetFromMap(new ConcurrentHashMap<>(initialCapacity));
        historyTaskSet = Collections.newSetFromMap(new ConcurrentHashMap<>(initialCapacity * 10));
    }

    public SetUniqueScheduler() {
       this(1000);
    }

    @Override
    public boolean isEmpty() {
        return taskSet.isEmpty();
    }

    @Override
    public HttpRequest take() {
        Iterator<HttpRequest> iterator = taskSet.iterator();
        if (iterator.hasNext()) {
            HttpRequest next = iterator.next();
            historyTaskSet.add(next.getUrl());
            return next;
        }
        return null;
    }

    @Override
    public boolean put(HttpRequest httpRequest) {
        Assert.notNull(httpRequest, "the  argument:[httpRequest] is required,it must not be null");
        return !historyTaskSet.contains(httpRequest.getUrl()) && taskSet.add(httpRequest);

    }
}
