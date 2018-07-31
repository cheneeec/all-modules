package com.earnest.crawler.core.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;


import java.io.Serializable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ToString
@Getter
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
    public Map<String,String> getHeaders() {
        if (CollectionUtils.isEmpty(headers)) {
            headers = Collections.singletonMap(Browser.USER_AGENT, Browser.GOOGLE.userAgent());
        }
        return this.headers;
    }


    public abstract String getMethod();

    @Override
    public AbstractHttpRequest clone() {

        return JSONObject.parseObject(JSONObject.toJSONString(this), this.getClass());

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractHttpRequest request = (AbstractHttpRequest) o;
        return ignoreJavascript == request.ignoreJavascript &&
                ignoreHTMLHead == request.ignoreHTMLHead &&
                ignoreCss == request.ignoreCss &&
                Objects.equals(url, request.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, ignoreJavascript, ignoreHTMLHead, ignoreCss);
    }

    @Override
    public int compareTo(HttpRequest o) {
        return Long.compare(this.priority, o.getPriority());
    }

}
