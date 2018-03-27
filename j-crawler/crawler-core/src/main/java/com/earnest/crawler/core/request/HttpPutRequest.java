package com.earnest.crawler.core.request;

public class HttpPutRequest extends AbstractHttpRequest {

    private static final String METHOD = "PUT";

    public HttpPutRequest(String uri) {
        super();
        this.url = uri;
    }


    @Override
    public String getMethod() {
        return METHOD;
    }
}
