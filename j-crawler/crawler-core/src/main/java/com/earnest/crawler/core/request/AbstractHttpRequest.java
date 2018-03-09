package com.earnest.crawler.core.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;


import java.io.Serializable;

import java.util.Map;

@Setter
public abstract class AbstractHttpRequest implements HttpRequest, Comparable<HttpRequest>, Serializable {

    private static final long serialVersionUID = -7284636094595149962L;

    protected String url;

    protected String charset;

    protected Map<String, String> parameters;

    protected Map<String, String> cookies;
    //Referer
    protected Map<String, String> headers;

    protected HttpProxy httpProxy;

    protected long priority;

    protected int connectTimeout;



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

    public abstract String getMethod();

    @Override
    public Object clone() {

        return JSONObject.parse(JSONObject.toJSONString(this));
    }

    @Override
    public int compareTo(HttpRequest o) {
        return Long.compare(this.priority, o.getPriority());
    }
}
