package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.request.HttpRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class HttpClientContextHolder {

    private static final Map<Downloader, HttpClientContext> httpClientContexts = new ConcurrentHashMap<>();


    /*public static HttpClientContext getHttpClientContext(String domain) {
        Assert.hasText(domain, "domain is empty or null");
        return httpClientContexts.get(domain);
    }


    public static HttpClientContext getHttpClientContext(HttpRequest request) {
        Assert.notNull(request, "request is null");

        return getHttpClientContext(URI.create(request.getUrl()).getHost());
    }

    public static HttpClientContext setHttpClientContext(String domain, HttpClientContext httpClientContext) {
        return httpClientContexts.put(domain, httpClientContext);
    }*/

 /*   public static HttpClientContext get(Downloader downloader) {
        return httpClientContexts.get(downloader);
    }

    public static HttpClientContext create(HttpRequest request) {

    }*/

}
