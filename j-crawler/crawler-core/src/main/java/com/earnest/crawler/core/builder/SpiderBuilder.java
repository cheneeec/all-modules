package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.crawler.Spider;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;

public class SpiderBuilder implements Builder<Spider> {

    //起点配置
    private final StartingPointConfigurer startingPoint = new StartingPointConfigurer(this);
    //全局的请求设置
    private final GlobalRequestConfigurer globalRequest = new GlobalRequestConfigurer(this);
    //过滤配置
    private final PipelineConfigurer pipelines = new PipelineConfigurer(this);
    //新的请求提取器
    private final HttpUriRequestExtractorConfigurer uriRequestExtractorConfigurer = new HttpUriRequestExtractorConfigurer(this);
    //
    private final CookieStore cookieStore = new BasicCookieStore();

    private final HttpClientContext clientContext = new HttpClientContext();

    public StartingPointConfigurer startingPoint() {
        return startingPoint;
    }

    public GlobalRequestConfigurer gloabl() {
        return globalRequest;
    }

    public PipelineConfigurer pipelines() {
        return pipelines;
    }

    public HttpUriRequestExtractorConfigurer extract() {
        return uriRequestExtractorConfigurer;
    }


    @Override
    public Spider build() {
        return null;
    }
}
