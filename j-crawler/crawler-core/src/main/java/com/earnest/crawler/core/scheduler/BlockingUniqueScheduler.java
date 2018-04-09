package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.downloader.listener.DownloadListener;
import com.earnest.crawler.core.event.DownloadErrorEvent;
import com.earnest.crawler.core.event.DownloadSuccessEvent;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
public class BlockingUniqueScheduler implements Scheduler, DownloadListener {

    private final Set<HttpRequest> taskSet;
    private final Set<String> historyTaskSet;
    private final Set<DownloadErrorEvent> errorTaskSet;

    private final ReentrantLock lock = new ReentrantLock();
    //取值条件
    private final Condition getCondition;

    public BlockingUniqueScheduler(int initialCapacity) {
        taskSet = new HashSet<>(initialCapacity);
        historyTaskSet = new HashSet<>(initialCapacity * 10);
        errorTaskSet = new HashSet<>(initialCapacity / 10);
        getCondition = lock.newCondition();

    }

    public BlockingUniqueScheduler() {
        this(10000);
    }

    @Override
    public Set<DownloadErrorEvent> getDownloadErrorEventSet() {
        return errorTaskSet;
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
    public boolean addAll(Collection<HttpRequest> httpRequests) {
        if (CollectionUtils.isEmpty(httpRequests)) return true;
        Set<HttpRequest> filterHistoryHttpRequests = filterHistoryHttpRequests(httpRequests);
        if (CollectionUtils.isEmpty(filterHistoryHttpRequests)) return true;
        try {
            lock.lock();
            int originalTaskSize = taskSet.size();
            taskSet.addAll(filterHistoryHttpRequests);
            int newTaskSize = taskSet.size();
            while (newTaskSize != originalTaskSize) {
                getCondition.signal();
                newTaskSize--;
            }
            return Objects.equals(originalTaskSize, newTaskSize);
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
            lock.lock();
            if (taskSet.isEmpty()) getCondition.await();
            Iterator<HttpRequest> iterator = taskSet.iterator();
            HttpRequest next = iterator.next();
            iterator.remove();
            //
            //将其也添加到historyTaskSet中
            historyTaskSet.add(next.getUrl());
            return next;
        } catch (InterruptedException e) {
            log.error("Interrupted when getting a httpRequest,error:{}", e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean put(HttpRequest httpRequest) {

        try {
            lock.lock();
            boolean add = taskSet.add(httpRequest);
            if (add) {
                getCondition.signal();
            }
            return add;
        } finally {
            lock.unlock();
        }


    }

    @Override
    public void onSuccess(DownloadSuccessEvent successEvent) {
        //ignored
    }

    @Override
    public void onError(DownloadErrorEvent errorEvent) {
        try {
            lock.lock();
            errorTaskSet.add(errorEvent);
        } finally {
            lock.unlock();
        }
    }


}
