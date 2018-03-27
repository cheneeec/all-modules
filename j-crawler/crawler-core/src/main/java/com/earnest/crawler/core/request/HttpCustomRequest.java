package com.earnest.crawler.core.request;

public class HttpCustomRequest extends AbstractHttpRequest {
    private final String method;

    public HttpCustomRequest(String method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method;
    }
}
