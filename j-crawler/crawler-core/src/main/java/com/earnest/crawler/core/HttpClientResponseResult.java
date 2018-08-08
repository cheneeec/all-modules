package com.earnest.crawler.core;

import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Map;

@Setter
@NoArgsConstructor
public class HttpClientResponseResult<T> implements HttpResponseResult<T> {

    private int status;
    private boolean success;
    private HttpUriRequest httpRequest;
    private Map<String, String> Headers;
    private Map<String, String> cookies;
    private String charset;
    private T content;

    private String reason;


    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public HttpUriRequest getHttpRequest() {
        return httpRequest;
    }

    @Override
    public Map<String, String> getHeaders() {
        return Headers;
    }

    @Override
    public String getCharset() {
        return charset;
    }

    @Override
    public T getContent() {
        return content;
    }


    @Override
    public Map<String, String> getCookies() {
        return cookies;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
