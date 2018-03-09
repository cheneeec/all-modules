package com.earnest.crawler.core.request;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HttpGetRequest extends AbstractHttpRequest {

    private static final String METHOD = "GET";

    public HttpGetRequest(String uri) {
        super();
        this.url = uri;
    }


    @Override
    public String getMethod() {
        return METHOD;
    }


}
