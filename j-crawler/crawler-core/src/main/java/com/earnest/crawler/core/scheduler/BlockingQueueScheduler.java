package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.downloader.DownloadListener;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


@AllArgsConstructor
@Slf4j
public class BlockingQueueScheduler implements Scheduler, DownloadListener {

    private final BlockingQueue<HttpRequest> taskQueue;

    private final Set<String> historyTaskQueue = new ConcurrentSkipListSet<>();

    private final Set<HttpRequest> errorHttpRequestQueue = new ConcurrentSkipListSet<>();

    public BlockingQueueScheduler(int size) {

        this(new LinkedBlockingQueue<>(size));

    }

    public BlockingQueueScheduler() {
        this(10000);
    }



    /*@Override
    public boolean offer(HttpRequest httpRequest) {
        if (!historyTaskQueue.contains(httpRequest.getUrl())) {
            log.info("get a new link:{}", httpRequest.getUrl());
            return taskQueue.offer(httpRequest);
        }
        return false;
    }*/


    /*@Override
    public void put(HttpRequest httpRequest) throws InterruptedException {
        if (!historyTaskQueue.contains(httpRequest.getUrl())) {
            log.info("get a new link:{}", httpRequest.getUrl());
            taskQueue.put(httpRequest);
        }
    }*/



    /*@Override
    public HttpRequest take() throws InterruptedException {
        return taskQueue.take();
    }*/
/*
    @Override
    public HttpRequest poll(long timeout, TimeUnit unit) throws InterruptedException {
        return taskQueue.poll(timeout, unit);
    }*/

    /*@Override
    public int remainingCapacity() {
        return taskQueue.remainingCapacity();
    }

    @Override
    public int drainTo(Collection<? super HttpRequest> c) {
        return taskQueue.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super HttpRequest> c, int maxElements) {
        return taskQueue.drainTo(c, maxElements);
    }
*/
    @Override
    public HttpRequest poll() {
        return taskQueue.poll();
    }

    @Override
    public HttpRequest peek() {
        return taskQueue.peek();
    }


    @Override
    public void onSuccess(HttpResponse httpResponse) {
        historyTaskQueue.add(httpResponse.getHttpRequest().getUrl());
    }

    @Override
    public boolean offer(HttpRequest httpRequest, long timeout, TimeUnit unit) throws InterruptedException {
        if (!historyTaskQueue.contains(httpRequest.getUrl())) {
            log.info("get a new link:{}", httpRequest.getUrl());
            return taskQueue.offer(httpRequest, timeout, unit);
        }
        return false;
    }

    public boolean offer(HttpRequest httpRequest) {
        if (!historyTaskQueue.contains(httpRequest.getUrl())) {
            log.info("get a new link:{}", httpRequest.getUrl());
            return taskQueue.offer(httpRequest);
        }
        return false;
    }

    @Override
    public void onError(HttpRequest httpRequest, Exception e) {
        errorHttpRequestQueue.add(httpRequest);
    }

    @Override
    public Set<HttpRequest> getErrorHttpRequestQueue() {
        return errorHttpRequestQueue;
    }

    @Override
    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

    @Override
    public boolean addAll(Collection<HttpRequest> c) {
        return taskQueue.addAll(c);
    }


}
