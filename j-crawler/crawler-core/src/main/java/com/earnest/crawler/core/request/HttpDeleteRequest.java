package com.earnest.crawler.core.request;

public class HttpDeleteRequest extends AbstractHttpRequest {
    private static final String METHOD = "DELETE";

    public HttpDeleteRequest(String uri) {
        super();
        this.url = uri;
    }


    @Override
    public String getMethod() {
        return METHOD;
    }
}
