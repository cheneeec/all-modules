package com.earnest.crawler.core.downloader.listener;

import com.earnest.crawler.core.event.DownloadErrorEvent;
import com.earnest.crawler.core.event.DownloadSuccessEvent;


import java.util.EventListener;


public interface DownloadListener extends EventListener {
    void onSuccess(DownloadSuccessEvent successEvent);

    void onError(DownloadErrorEvent errorEvent);
}
