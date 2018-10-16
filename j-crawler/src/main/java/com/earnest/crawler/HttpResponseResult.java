package com.earnest.crawler;

import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

public interface HttpResponseResult<T> {

    int getStatus();

    boolean isSuccess();

    HttpUriRequest getHttpRequest();

    Map<String, String> getHeaders();

    String getCharset();

    T getContent();

    Map<String, String> getCookies();

    String getReason();


}
