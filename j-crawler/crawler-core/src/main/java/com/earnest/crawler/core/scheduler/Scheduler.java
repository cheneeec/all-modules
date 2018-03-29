package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.request.HttpRequest;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Scheduler {
    Set<HttpRequest> getErrorHttpRequestSet();

    boolean isEmpty();

    boolean addAll(Collection<HttpRequest> c);

    HttpRequest poll();

    HttpRequest peek();

    HttpRequest take();

    boolean offer(HttpRequest httpRequest);

    boolean offer(HttpRequest httpRequest, long timeout, TimeUnit unit) throws InterruptedException;
}
