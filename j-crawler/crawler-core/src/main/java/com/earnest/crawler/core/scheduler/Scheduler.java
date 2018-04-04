package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.event.DownloadErrorEvent;
import com.earnest.crawler.core.request.HttpRequest;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface Scheduler {
    /**
     * 获取请求失败的集合
     *
     * @return
     */
    Set<DownloadErrorEvent> getDownloadErrorEventSet();

    /**
     * 判断任务队列是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 添加所有请求
     *
     * @param httpRequests
     * @return
     */
    default boolean addAll(Collection<HttpRequest> httpRequests) {
        httpRequests.forEach(this::put);
        return true;
    }

    /**
     * 获取一个请求
     *
     * @return
     */
    HttpRequest take();

    /**
     * 加入一个请求
     *
     * @param httpRequest
     * @return
     */
    boolean put(HttpRequest httpRequest);

}
