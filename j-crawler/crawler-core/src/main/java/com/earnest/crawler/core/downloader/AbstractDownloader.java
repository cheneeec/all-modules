package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.downloader.listener.DownloadListener;
import com.earnest.crawler.core.event.DownloadErrorEvent;
import com.earnest.crawler.core.event.DownloadSuccessEvent;

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
    public void onSuccess(DownloadSuccessEvent successEvent) {
        if (!downloadListeners.isEmpty())
            downloadListeners.forEach(downloadListener -> downloadListener.onSuccess(successEvent));
    }

    @Override
    public void onError(DownloadErrorEvent errorEvent) {
        if (!downloadListeners.isEmpty())
            downloadListeners.forEach(downloadListener -> downloadListener.onError(errorEvent));
    }
}
