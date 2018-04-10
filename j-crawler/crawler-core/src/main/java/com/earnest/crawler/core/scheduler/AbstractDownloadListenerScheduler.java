package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.event.DownloadErrorEvent;
import com.earnest.crawler.core.event.DownloadSuccessEvent;

import java.util.Set;

public abstract class AbstractDownloadListenerScheduler implements Scheduler {

    protected final Set<DownloadSuccessEvent> downloadSuccessEventSet;
    protected final Set<DownloadErrorEvent> downloadErrorEventSet;


    public AbstractDownloadListenerScheduler(Set<DownloadSuccessEvent> downloadSuccessEventSet, Set<DownloadErrorEvent> downloadErrorEventSet) {
        this.downloadSuccessEventSet = downloadSuccessEventSet;
        this.downloadErrorEventSet = downloadErrorEventSet;
    }

    @Override
    public Set<DownloadErrorEvent> getDownloadErrorEventSet() {
        return downloadErrorEventSet;
    }

    @Override
    public Set<DownloadSuccessEvent> getDownloadSuccessEventSet() {
        return downloadSuccessEventSet;
    }


    @Override
    public void onSuccess(DownloadSuccessEvent successEvent) {
        downloadSuccessEventSet.add(successEvent);
    }

    @Override
    public void onError(DownloadErrorEvent errorEvent) {
        downloadErrorEventSet.add(errorEvent);
    }
}
