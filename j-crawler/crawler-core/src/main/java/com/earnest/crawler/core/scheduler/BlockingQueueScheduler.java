package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.request.HttpRequest;
import lombok.AllArgsConstructor;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class BlockingQueueScheduler extends AbstractQueue<HttpRequest> implements Scheduler {

    private final BlockingQueue<HttpRequest> taskQueue;
    private final Set<HttpRequest> historyTaskQueue;

    public BlockingQueueScheduler(int size) {
        this(new LinkedBlockingQueue<>(size), new ConcurrentSkipListSet<>());
    }

    public BlockingQueueScheduler() {
        this(10000);
    }

    @Override
    public Iterator<HttpRequest> iterator() {
        return taskQueue.iterator();
    }

    @Override
    public int size() {
        return taskQueue.size();
    }

    @Override
    public boolean offer(HttpRequest httpRequest) {

        return taskQueue.offer(httpRequest);
    }

    @Override
    public void put(HttpRequest httpRequest) throws InterruptedException {
        taskQueue.put(httpRequest);
    }

    @Override
    public boolean offer(HttpRequest httpRequest, long timeout, TimeUnit unit) throws InterruptedException {
        return taskQueue.offer(httpRequest, timeout, unit);
    }

    @Override
    public HttpRequest take() throws InterruptedException {
        return taskQueue.take();
    }

    @Override
    public HttpRequest poll(long timeout, TimeUnit unit) throws InterruptedException {
        return taskQueue.poll(timeout, unit);
    }

    @Override
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

    @Override
    public HttpRequest poll() {
        return taskQueue.poll();
    }

    @Override
    public HttpRequest peek() {
        return taskQueue.peek();
    }


}
