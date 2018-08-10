package com.earnest.crawler.core.scheduler1;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


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

    @Override
    public void put(Collection<HttpUriRequest> httpUriRequest) {
        queue.addAll(httpUriRequest);
    }
}
