package com.earnest.crawler.core.request;


import lombok.Data;
import org.apache.http.HttpHost;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public interface HttpRequest extends Cloneable {

    String getUrl();

    Map<String, String> getParameters();

    /**
     * user-agent
     *
     * @return
     */
    Map<String, String> getHeaders();

    String getCharset();

    Map<String, String> getCookies();

    long getPriority();

    String getMethod();

    HttpProxy getHttpProxy();

    int getConnectTimeout();

    boolean ignoreJavascript();

    boolean ignoreHTMLHead();

    boolean ignoreCss();




    @Data
    class HttpProxy {
        private HttpHost httpHost;

        private AtomicLong successCount;

        private AtomicLong failureCount;

        private String username;
        private String password;

        private String src = "custom";//来源
    }
}
