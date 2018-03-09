package com.earnest.crawler.core.request;


public class HttpPostRequest extends AbstractHttpRequest {

    private static final String METHOD = "POST";

    @Override
    public String getMethod() {
        return METHOD;
    }
}
