package com.earnest.crawler.core;

import com.earnest.crawler.core.request.HttpRequest;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
public abstract class BasicHttpResponseResult<T> implements HttpResponseResult<T> {

    private int status;
    private boolean success;
    private HttpRequest httpRequest;
    private Map<String, List<String>> Headers;
    private Map<String, String> cookies;
    private String charset;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return Headers;
    }

    @Override
    public String getCharset() {
        return charset;
    }



    @Override
    public Map<String, String> getCookies() {
        return cookies;
    }
}
