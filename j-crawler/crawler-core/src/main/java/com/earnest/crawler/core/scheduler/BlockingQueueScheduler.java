package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.downloader.DownloadListener;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@AllArgsConstructor
@Slf4j
public class BlockingQueueScheduler implements Scheduler, DownloadListener {

    private final BlockingQueue<HttpRequest> taskQueue;

    private final Set<String> historyUrlSet = new HashSet<>();

    private final Set<HttpRequest> errorHttpRequestSet = new HashSet<>();


    public BlockingQueueScheduler(int size) {
        this(new LinkedBlockingQueue<>(size));
    }

    public BlockingQueueScheduler() {
        this(10000);
    }

    @Override
    public HttpRequest poll() {
        return taskQueue.poll();
    }

    @Override
    public HttpRequest peek() {
        return taskQueue.peek();
    }

    @Override
    public HttpRequest take() {

        try {
            HttpRequest httpRequest = taskQueue.take();
            historyUrlSet.add(httpRequest.getUrl());
            return httpRequest;
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Interrupted when getting a httpRequest,error:{}", e.getMessage());
        }
        return null;
    }


    @Override
    public void onSuccess(HttpResponse httpResponse) {

    }

    @Override
    public boolean offer(HttpRequest httpRequest, long timeout, TimeUnit unit) throws InterruptedException {
        if (!historyUrlSet.contains(httpRequest.getUrl())) {
            log.info("get a new link:{}", httpRequest.getUrl());
            return taskQueue.offer(httpRequest, timeout, unit);
        }
        return false;
    }

    public boolean offer(HttpRequest httpRequest) {
        if (!historyUrlSet.contains(httpRequest.getUrl())) {
            log.info("get a new link:{}", httpRequest.getUrl());
            return taskQueue.offer(httpRequest);
        }
        return false;
    }

    @Override
    public void onError(HttpRequest httpRequest, Exception e) {
        errorHttpRequestSet.add(httpRequest);
    }

    @Override
    public Set<HttpRequest> getErrorHttpRequestSet() {
        return errorHttpRequestSet;
    }

    @Override
    public boolean isEmpty() {
        return taskQueue.isEmpty();
    }

    @Override
    public boolean addAll(Collection<HttpRequest> c) {
        return taskQueue.addAll(c.parallelStream().filter(h -> !historyUrlSet.contains(h))
                .peek(i -> log.info("get a new Url:{}", i.getUrl()))
                .collect(Collectors.toSet()));
    }


}
