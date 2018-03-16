package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.request.HttpRequest;
import lombok.AllArgsConstructor;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@AllArgsConstructor
public class BlockingQueueScheduler extends AbstractQueue<HttpRequest> implements Scheduler {

    private final BlockingQueue<HttpRequest> queue;

    public BlockingQueueScheduler(int size) {
        this(new LinkedBlockingQueue<>(size));
    }

    public BlockingQueueScheduler() {
        this(new LinkedBlockingQueue<>(1000));
    }

    @Override
    public Iterator<HttpRequest> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean offer(HttpRequest httpRequest) {

        return queue.offer(httpRequest);
    }

    @Override
    public HttpRequest poll() {
        return queue.poll();
    }

    @Override
    public HttpRequest peek() {
        return queue.peek();
    }
}
