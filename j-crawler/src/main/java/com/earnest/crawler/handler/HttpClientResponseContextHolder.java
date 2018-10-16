package com.earnest.crawler.handler;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 为{@link org.apache.http.HttpResponse}保存对应的请求{@link org.apache.http.client.methods.HttpUriRequest}。
 */
public abstract class HttpClientResponseContextHolder {

    private static final Map<HttpResponse, HttpUriRequest> httpResponseHttpUriRequestContext = new ConcurrentHashMap<>(500);

    public static HttpUriRequest getHttpUriRequest(HttpResponse httpResponse) {
        return httpResponseHttpUriRequestContext.get(httpResponse);
    }

    public static HttpUriRequest setHttpUriRequest(HttpResponse httpResponse, HttpUriRequest httpUriRequest) {
        return httpResponseHttpUriRequestContext.put(httpResponse, httpUriRequest);
    }

}
