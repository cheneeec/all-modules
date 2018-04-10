package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.downloader.listener.DownloadListener;
import com.earnest.crawler.core.event.DownloadErrorEvent;
import com.earnest.crawler.core.event.DownloadSuccessEvent;
import com.earnest.crawler.core.request.HttpRequest;

import java.util.Collection;
import java.util.Set;


public interface Scheduler extends DownloadListener {
    /**
     * 获取请求失败的集合
     *
     * @return
     */
    Set<DownloadErrorEvent> getDownloadErrorEventSet();


    Set<DownloadSuccessEvent> getDownloadSuccessEventSet();

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
