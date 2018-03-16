package com.earnest.crawler.core.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;


import java.io.Serializable;

import java.util.Map;
import java.util.Objects;

@Setter
public abstract class AbstractHttpRequest implements HttpRequest, Comparable<HttpRequest>, Serializable {

    private static final long serialVersionUID = -7284636094595149962L;

    protected String url;

    protected String charset = "UTF-8";

    protected Map<String, String> parameters;

    protected Map<String, String> cookies;
    //Referer
    protected Map<String, String> headers;

    protected HttpProxy httpProxy;

    protected long priority;

    protected int connectTimeout;

    protected boolean ignoreJavascript = true;
    protected boolean ignoreHTMLHead = true;
    protected boolean ignoreCss = true;


    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }

    @Override
    public Map<String, String> getHeaders() {
        if (Objects.isNull(headers)) {
            return getDefaultHttpRequestHeader();
        }
        return this.headers;
    }


    @Override
    public String getCharset() {
        return this.charset;
    }

    @Override
    public Map<String, String> getCookies() {
        return this.cookies;
    }

    @Override
    public long getPriority() {
        return this.priority;
    }

    @Override
    public HttpProxy getHttpProxy() {
        return this.httpProxy;
    }

    @Override
    public int getConnectTimeout() {
        return this.connectTimeout;
    }


    @Override
    public boolean ignoreJavascript() {
        return ignoreJavascript;
    }

    @Override
    public boolean ignoreHTMLHead() {
        return ignoreHTMLHead;
    }

    public abstract String getMethod();


    @Override
    public boolean ignoreCss() {
        return ignoreCss;
    }

    @Override
    public AbstractHttpRequest clone() {
        /*try {
            AbstractHttpRequest request = (AbstractHttpRequest) super.clone();
            request.setCookies(new HashMap<>(request.getCookies()));
            request.setHeaders(new HashMap<>(request.getHeaders()));

            return request;
        } catch (CloneNotSupportedException ignored) {


        }*/
        return (AbstractHttpRequest) JSONObject.parse(JSONObject.toJSONString(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractHttpRequest request = (AbstractHttpRequest) o;

        return this.url.equals((request.getUrl()));
    }


    @Override
    public int compareTo(HttpRequest o) {
        return Long.compare(this.priority, o.getPriority());
    }
}
