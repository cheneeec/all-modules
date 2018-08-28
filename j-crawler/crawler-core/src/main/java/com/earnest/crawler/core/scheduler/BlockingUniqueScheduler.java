package com.earnest.crawler.core.scheduler;


import com.earnest.crawler.core.exception.TakeTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 阻塞的调度器，并且能够保证请求唯一。
 */
@Slf4j
public class BlockingUniqueScheduler implements Scheduler {
    //任务值
    private final Set<HttpUriRequest> taskSet;
    //历史值
    private final Set<String> historyTaskSet;
    //取值条件
    private final ReentrantLock lock = new ReentrantLock();
    //取值条件
    private final Condition getCondition;
    //阻塞超时时间
    private final int timeout;

    private final static int DEFAULT_TIMEOUT=5000;

    public BlockingUniqueScheduler(int initialCapacity, int timeout) {
        taskSet = new HashSet<>(initialCapacity);
        historyTaskSet = new HashSet<>(initialCapacity * 10);
        this.timeout = timeout;
        getCondition = lock.newCondition();
    }

    public BlockingUniqueScheduler(int timeout) {
        this(10000, timeout == 0 ? DEFAULT_TIMEOUT : timeout);
    }

    public BlockingUniqueScheduler() {
        this(DEFAULT_TIMEOUT);
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
    public HttpUriRequest take() {
        try {
            lock.lockInterruptibly();
            if (taskSet.isEmpty()) {
                log.debug("The number of threads currently waiting is {}", lock.getWaitQueueLength(getCondition)+1);
                //等待取值
                boolean await = getCondition.await(timeout, TimeUnit.MILLISECONDS);
                //等待到超时时间
                if (!await) {
                    throw new TakeTimeoutException("the time is out");
                }
                return obtainNewHttpUriRequest();
            } else {
                return obtainNewHttpUriRequest();
            }
        } catch (InterruptedException e) {
            log.error("Interrupted when getting a httpUriRequest,error:{}", e.getMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }

    private HttpUriRequest obtainNewHttpUriRequest() {
        Iterator<HttpUriRequest> httpUriRequestIterator = taskSet.iterator();
        if (httpUriRequestIterator.hasNext()) {
            HttpUriRequest httpUriRequest = httpUriRequestIterator.next();
            historyTaskSet.add(httpUriRequest.getURI().toString());
            httpUriRequestIterator.remove();
            return httpUriRequest;
        }
        return null;
    }

    @Override
    public boolean put(HttpUriRequest httpUriRequest) {
        if (httpUriRequest == null) return true;
        String uri = httpUriRequest.getURI().toString();
        try {
            lock.lock();
            if (historyTaskSet.contains(uri)) {
                log.trace("URI:{} is already in the history set", uri);
                return false;
            }
            if (taskSet.add(httpUriRequest)) {
                log.trace("The number of threads currently waiting is {}", lock.getWaitQueueLength(getCondition)-1);
                getCondition.signal();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Collection<HttpUriRequest> httpUriRequests) {
        if (CollectionUtils.isEmpty(httpUriRequests)) return;

        try {
            lock.lock();
            //过滤历史请求
            Set<HttpUriRequest> httpUriRequestSet = httpUriRequests.stream()
                    .filter(httpRequest -> !historyTaskSet.contains(httpRequest.getURI().toString()))
                    .collect(Collectors.toSet());

            if (CollectionUtils.isEmpty(httpUriRequestSet)) return;

            int originalTaskSize = taskSet.size();
            taskSet.addAll(httpUriRequestSet);
            int newTaskSize = taskSet.size();
            while (newTaskSize != originalTaskSize) {
                getCondition.signal();
                newTaskSize--;
            }
        } finally {
            lock.unlock();
        }

    }
}
