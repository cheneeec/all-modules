package com.earnest.crawler.core.downloader.proxy;

import com.earnest.crawler.core.downloader.HttpClientDownloader;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import org.apache.http.client.HttpClient;

import java.util.function.Supplier;

public class ProxyHttpClientDownloader extends HttpClientDownloader {

    private final Supplier<HttpRequest.HttpProxy> httpProxySupplier;

    public ProxyHttpClientDownloader(HttpClient httpClient, Supplier<HttpRequest.HttpProxy> httpProxySupplier) {
        super(httpClient);
        this.httpProxySupplier = httpProxySupplier;
    }

    public ProxyHttpClientDownloader(Supplier<HttpRequest.HttpProxy> httpProxySupplier) {
        this.httpProxySupplier = httpProxySupplier;
    }

    @Override
    public HttpResponse download(HttpRequest request) {
        request.setHttpProxy(httpProxySupplier.get());
        return super.download(request);
    }


}
