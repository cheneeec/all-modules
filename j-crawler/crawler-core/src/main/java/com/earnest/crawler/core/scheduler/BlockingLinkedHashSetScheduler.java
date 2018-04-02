package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.downloader.DownloadListener;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Slf4j
public class BlockingLinkedHashSetScheduler implements Scheduler, DownloadListener {
    private final Map<HttpRequest, Object> taskSet;
    private final Set<String> historyTaskSet;
    private final Set<HttpRequest> errorTaskSet;

    private final ReentrantReadWriteLock readWriteLock;
    //取值条件
    private final Condition getCondition;

    private final Object dumpValue = new Object();


    public BlockingLinkedHashSetScheduler(int initialCapacity) {

        taskSet = new ConcurrentHashMap<>(initialCapacity);
        historyTaskSet = new HashSet<>(initialCapacity * 10);
        errorTaskSet = new LinkedHashSet<>(initialCapacity / 10);

        readWriteLock = new ReentrantReadWriteLock();
        getCondition = readWriteLock.writeLock().newCondition();

    }

    public BlockingLinkedHashSetScheduler() {
        this(10000);
    }

    @Override
    public Set<HttpRequest> getErrorHttpRequestSet() {
        return errorTaskSet;
    }

    @Override
    public boolean isEmpty() {
        return taskSet.isEmpty();
    }

    @Override
    public boolean addAll(Collection<HttpRequest> httpRequests) {
        if (CollectionUtils.isEmpty(httpRequests)) return true;

        Map<HttpRequest, Object> filterHistoryHttpRequests = filterHistoryHttpRequests(httpRequests);

        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            int originalTaskSize = taskSet.size();
            taskSet.putAll(filterHistoryHttpRequests);
            int newTaskSize = taskSet.size();
            while (newTaskSize != originalTaskSize) {
                getCondition.signal();
                newTaskSize--;
            }
            return Objects.equals(originalTaskSize, newTaskSize);
        } finally {
            writeLock.unlock();
        }
    }

    private Map<HttpRequest, Object> filterHistoryHttpRequests(Collection<HttpRequest> httpRequests) {
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            return httpRequests.parallelStream()
                    .filter(httpRequest -> !historyTaskSet.contains(httpRequest.getUrl()))
                    .collect(Collectors.toConcurrentMap(h -> h, h -> dumpValue));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public HttpRequest take() {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            if (isEmpty()) {
                getCondition.await();
            }
            Iterator<HttpRequest> iterator = taskSet.keySet().iterator();
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
            writeLock.unlock();
        }
    }

    @Override
    public boolean put(HttpRequest httpRequest) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();

        try {
            boolean put = Objects.isNull(taskSet.put(httpRequest, dumpValue));
            if (put) {
                getCondition.signal();
            }
            return put;
        } finally {
            writeLock.unlock();
        }


    }

    @Override
    public void onSuccess(HttpResponse httpResponse) {
        //忽略这个方法
    }

    @Override
    public void onError(HttpRequest httpRequest, Exception e) {
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            errorTaskSet.add(httpRequest);
        } finally {
            writeLock.unlock();
        }
    }

}
