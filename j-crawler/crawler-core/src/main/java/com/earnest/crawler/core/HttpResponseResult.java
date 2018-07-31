package com.earnest.crawler.core;

import com.earnest.crawler.core.request.HttpRequest;

import java.util.List;
import java.util.Map;

public interface HttpResponseResult<T> {

    int getStatus();

    boolean isSuccess();

    HttpRequest getHttpRequest();

    Map<String, List<String>> getHeaders();

    String getCharset();

    T getContent();

    Map<String, String> getCookies();

}
