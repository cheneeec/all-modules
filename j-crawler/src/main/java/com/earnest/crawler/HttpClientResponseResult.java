package com.earnest.crawler;

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


    public HttpClientResponseResult(HttpClientResponseResult<?> httpClientResponseResult) {
        this.status = httpClientResponseResult.getStatus();
        this.success = httpClientResponseResult.isSuccess();
        this.httpRequest = httpClientResponseResult.getHttpRequest();
        this.Headers = httpClientResponseResult.getHeaders();
        this.cookies = httpClientResponseResult.getCookies();
        this.charset = httpClientResponseResult.getCharset();
        this.reason = httpClientResponseResult.getReason();
        this.content = null;

    }


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
