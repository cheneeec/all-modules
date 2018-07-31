package com.earnest.crawler.core.request;



import java.util.Map;

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

    void setHeaders(Map<String,String> headers);

    void setHttpProxy(HttpProxy httpProxy);

    void setPriority(long priority);

    void setConnectTimeout(int connectTimeout);

    void setIgnoreJavascript(boolean ignoreJavascript);

    void setIgnoreHTMLHead(boolean ignoreHTMLHead);

    void setIgnoreCss(boolean ignoreCss);




}
