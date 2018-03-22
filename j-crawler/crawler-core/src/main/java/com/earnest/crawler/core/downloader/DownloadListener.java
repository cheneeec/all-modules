package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;


public interface DownloadListener {
    void onSuccess(HttpResponse httpResponse);

    void onError(HttpRequest httpRequest,Exception e);
}
