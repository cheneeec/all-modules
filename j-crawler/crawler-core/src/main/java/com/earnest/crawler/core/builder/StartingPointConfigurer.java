package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.request.Browser;
import com.earnest.crawler.core.request.HttpRequest;
import org.jsoup.Connection;

import java.util.LinkedHashMap;
import java.util.Map;

public class StartingPointConfigurer extends AbstractSpiderConfigurer<HttpRequest> {

    private String from;
    private Map<String, String> cookies = new LinkedHashMap<>();

    private Map<String, String> headers = new LinkedHashMap<>();
    private String method;
    private Map<String, String> parameters = new LinkedHashMap<>();


    public StartingPointConfigurer from(String url) {
        this.from = url;
        return this;
    }

    public StartingPointConfigurer addCookie(String name, String value) {
        this.cookies.put(name, value);
        return this;
    }

    public StartingPointConfigurer addCookies(Map<String, String> cookies) {
        this.cookies.putAll(cookies);
        return this;
    }

    public StartingPointConfigurer addParameter(String name, String value) {
        this.parameters.put(name, value);
        return this;
    }

    public StartingPointConfigurer addParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
        return this;
    }

    public StartingPointConfigurer referer(String referer) {
        this.headers.put(Browser.REFERER, referer);
        return this;
    }

    public StartingPointConfigurer userAgent(String userAgent) {
        this.headers.put(Browser.USER_AGENT, userAgent);
        return this;
    }

    public StartingPointConfigurer method(Connection.Method method) {
        this.method = method.name();
        return this;
    }

    public StartingPointConfigurer contentType(String contentType) {
        this.headers.put(Browser.CONTENT_TYPE, contentType);
        return this;
    }


}
