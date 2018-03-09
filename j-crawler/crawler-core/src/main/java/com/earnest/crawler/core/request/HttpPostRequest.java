package com.earnest.crawler.core.request;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HttpPostRequest extends AbstractHttpRequest {

    private static final String METHOD = "POST";
    public HttpPostRequest(String uri) {
        super();
        this.url = uri;
    }
    @Override
    public String getMethod() {
        return METHOD;
    }
}
