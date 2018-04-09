package com.earnest.crawler.core.request;


import lombok.Data;
import org.apache.http.HttpHost;

import java.util.HashMap;
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

    boolean isIgnoreJavascript();

    boolean isIgnoreHTMLHead();

    boolean isIgnoreCss();

    void setUrl(String url);
    void setCharset(String charset);
    void setParameters(Map<String, String> parameters);
    void setCookies(Map<String, String> cookies);
    void setHeaders(Map<String, String> headers);
    void setHttpProxy(HttpProxy httpProxy);
    void setPriority(long priority);
    void setConnectTimeout(int connectTimeout);
    void setIgnoreJavascript(boolean ignoreJavascript);
    void setIgnoreHTMLHead(boolean ignoreHTMLHead);
    void setIgnoreCss(boolean ignoreCss);

    default Map<String, String> getDefaultHttpRequestHeader() {

        return new HashMap<String, String>(3) {{
            put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        }};
    }

    @Data
    class HttpProxy {
        private HttpHost httpHost;
        private String username;
        private String password;

    }
}
