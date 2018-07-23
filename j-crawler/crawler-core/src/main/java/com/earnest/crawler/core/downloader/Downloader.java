package com.earnest.crawler.core.downloader;


import com.earnest.crawler.core.downloader.listener.DownloadListener;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.PageResponse;


import java.io.Closeable;
import java.util.Set;

public interface Downloader extends Closeable {


    PageResponse download(HttpRequest request);

    Set<DownloadListener> getHttpDownloadListeners();

    void addDownloadListener(DownloadListener downloadListener);
}
