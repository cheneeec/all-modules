package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.downloader.DownloadListener;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractDownloadListenerScheduler implements Scheduler,DownloadListener {

    private final Set<String> historyUrlSet = new HashSet<>(10000);

    private final Set<HttpRequest> errorHttpRequestSet = new HashSet<>(100);


    @Override
    public Set<HttpRequest> getErrorHttpRequestSet() {

        return errorHttpRequestSet;
    }


    @Override
    public boolean addAll(Collection<HttpRequest> httpRequests) {
        return false;
    }

    @Override
    public HttpRequest take() {
        return null;
    }

    @Override
    public boolean put(HttpRequest httpRequest) {
        return false;
    }

    @Override
    public void onSuccess(HttpResponse httpResponse) {

    }

    @Override
    public void onError(HttpRequest httpRequest, Exception e) {

    }
}
