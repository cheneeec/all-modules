package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.response.HttpResponse;


public interface DownloadListener {
    void onSuccess(HttpResponse httpResponse);

    void onFailure(Exception e);
}
