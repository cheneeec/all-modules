package com.earnest.crawler.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.HttpHost;

@Data
@AllArgsConstructor
public class HttpProxy {
    private HttpHost httpHost;
    private String username;
    private String password;

    public HttpProxy(HttpHost httpHost) {
        this(httpHost, null, null);
    }
}
