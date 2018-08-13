package com.earnest.crawler.core.scheduler1;

import org.apache.http.client.methods.HttpUriRequest;

import java.util.Collection;

public interface Scheduler {

    boolean isEmpty();

    HttpUriRequest take();

    boolean put(HttpUriRequest httpUriRequest);

    void putAll(Collection<HttpUriRequest> httpUriRequests);

}
