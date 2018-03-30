package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractDownloader implements Downloader, DownloadListener {

    protected final Set<DownloadListener> downloadListeners;

    protected AbstractDownloader() {
        downloadListeners = new HashSet<>(5);
    }

    protected AbstractDownloader(Set<DownloadListener> downloadListeners) {
        this.downloadListeners = downloadListeners;
    }


    @Override
    public Set<DownloadListener> getHttpDownloadListeners() {
        return downloadListeners;
    }

    @Override
    public void addDownloadListener(DownloadListener downloadListener) {
        downloadListeners.add(downloadListener);
    }


    @Override
    public void onSuccess(HttpResponse httpResponse) {
        if (!downloadListeners.isEmpty()) {

            downloadListeners.forEach(s -> s.onSuccess(httpResponse));
        }
    }

    @Override
    public void onError(HttpRequest httpRequest, Exception e) {
        if (!downloadListeners.isEmpty()) {
            downloadListeners.forEach(s -> s.onError(httpRequest, e));
        }
    }
}
