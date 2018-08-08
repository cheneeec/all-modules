package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.exception.TakeTimeoutException;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
public class BlockingUniqueScheduler extends AbstractDownloadListenerScheduler implements BlockingScheduler {

    private final Set<HttpRequest> taskSet;
    private final Set<String> historyTaskSet;

    private final ReentrantLock lock = new ReentrantLock();
    //取值条件
    private final Condition getCondition;

    public BlockingUniqueScheduler(int initialCapacity) {
        super(new HashSet<>(initialCapacity * 10), new HashSet<>(initialCapacity / 10));
        taskSet = new HashSet<>(initialCapacity);
        historyTaskSet = new HashSet<>(initialCapacity * 10);
        getCondition = lock.newCondition();
    }

    public BlockingUniqueScheduler() {
        this(10000);
    }


    @Override
    public boolean isEmpty() {
        try {
            lock.lock();
            return taskSet.isEmpty();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public void addAll(Collection<HttpRequest> httpRequests) {
        if (CollectionUtils.isEmpty(httpRequests)) return;
        Set<HttpRequest> filterHistoryHttpRequests = filterHistoryHttpRequests(httpRequests);
        if (CollectionUtils.isEmpty(filterHistoryHttpRequests)) return;
        try {
            lock.lock();
            int originalTaskSize = taskSet.size();
            taskSet.addAll(filterHistoryHttpRequests);
            int newTaskSize = taskSet.size();
            while (newTaskSize != originalTaskSize) {
                getCondition.signal();
                newTaskSize--;
            }
            Objects.equals(originalTaskSize, newTaskSize);
        } finally {
            lock.unlock();
        }
    }

    private Set<HttpRequest> filterHistoryHttpRequests(Collection<HttpRequest> httpRequests) {
        try {
            lock.lock();
            return httpRequests.parallelStream()
                    .filter(httpRequest -> !historyTaskSet.contains(httpRequest.getUrl()))
                    .collect(Collectors.toSet());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public HttpRequest take() {
        try {
            return take(0, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("Interrupted when getting a httpRequest,error:{}", e.getMessage());
        } catch (TakeTimeoutException ignored) {
            //ignored,it is not happened
        }
        return null;
    }

    @Override
    public boolean put(HttpRequest httpRequest) {
        Assert.notNull(httpRequest, "the  argument:[httpRequest] is required,it must not be null");
        String url = httpRequest.getUrl();
        try {
            lock.lock();
            if (historyTaskSet.contains(url)) return false;
            boolean add = taskSet.add(httpRequest);
            if (add) getCondition.signal();
            return add;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public HttpRequest take(long time, TimeUnit unit) throws InterruptedException, TakeTimeoutException {
        try {
            lock.lock();
            if (taskSet.isEmpty()) {
                if (isNull(unit)) {
                    getCondition.await();
                } else {
                    boolean await = getCondition.await(time, unit);
                    if (!await) {
                        throw new TakeTimeoutException("the time is out");
                    }
                }
            }
            Iterator<HttpRequest> iterator = taskSet.iterator();
            if (iterator.hasNext()) {
                HttpRequest next = iterator.next();
                iterator.remove();
                //将其也添加到historyTaskSet中
                historyTaskSet.add(next.getUrl());
                return next;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
}
