package com.earnest.crawler.core.scheduler1;

import org.apache.http.client.methods.HttpUriRequest;

public interface Scheduler {

    boolean isEmpty();

    HttpUriRequest take();

    boolean put(HttpUriRequest httpUriRequest);

}
