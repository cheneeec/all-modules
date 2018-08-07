package com.earnest.crawler.core;

import org.apache.http.client.methods.HttpUriRequest;

import java.io.InputStream;
import java.util.Map;

public interface HttpResponseResult<T> {

    int getStatus();

    boolean isSuccess();

    HttpUriRequest getHttpRequest();

    Map<String, String> getHeaders();

    String getCharset();

    T getContent();

    Map<String, String> getCookies();



}
