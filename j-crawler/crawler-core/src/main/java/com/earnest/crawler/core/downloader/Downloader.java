package com.earnest.crawler.core.downloader;


import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;

public interface Downloader {



    HttpResponse download(HttpRequest request);

    boolean shutdown();
}
