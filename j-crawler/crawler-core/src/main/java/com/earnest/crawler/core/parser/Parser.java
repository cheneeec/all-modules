package com.earnest.crawler.core.parser;

import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.downloader.HttpClientDownloader;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;

import java.io.Closeable;
import java.util.Set;

public interface Parser extends Closeable {

    HttpResponseHandler getHttpResponseHandler();

    <T> Pipeline<T> getPipeline();

    Set<HttpRequest> getHttpRequests();

    default Downloader getDownloader() {
        return new HttpClientDownloader();
    }
}
