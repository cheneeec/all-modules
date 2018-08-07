package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.handler1.HttpClientEntityResponseHandler;
import com.earnest.crawler.core.pipeline.Pipeline;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;


public class SpiderBuilder implements Builder<Spider> {

    //起点配置
    private final StartingPointConfigurer startingPointConfigurer = new StartingPointConfigurer(this);
    //全局的请求设置
    private final GlobalRequestConfigurer globalRequestConfigurer = new GlobalRequestConfigurer(this);
    //过滤配置
    private final PipelineConfigurer pipelineConfigurer = new PipelineConfigurer(this);
    //新的请求提取器
    private final HttpUriRequestExtractorConfigurer httpUriRequestExtractorConfigurer = new HttpUriRequestExtractorConfigurer(this);
    //
    private final CookieStore cookieStore = new BasicCookieStore();

    private final HttpClientContext clientContext = new HttpClientContext();
    //调度器


    /**
     * 设置开始爬取的起点网址。
     *
     * @return
     */
    public StartingPointConfigurer startingPoint() {
        return startingPointConfigurer;
    }

    /**
     * 设置全局的配置信息。
     *
     * @return
     */
    public GlobalRequestConfigurer gloabl() {
        return globalRequestConfigurer;
    }

    /**
     * 设置管道处理器。
     *
     * @return
     */
    public PipelineConfigurer pipelines() {
        return pipelineConfigurer;
    }

    /**
     * 获取抓取的<code>url</code>配置。
     *
     * @return
     */
    public HttpUriRequestExtractorConfigurer fetchUrl() {
        return httpUriRequestExtractorConfigurer;
    }


    @Override
    public Spider build() {
        HttpUriRequest httpUriRequest = startingPointConfigurer.build();
        CloseableHttpClient closeableHttpClient = globalRequestConfigurer.build();
        HttpRequestExtractor httpRequestExtractor = httpUriRequestExtractorConfigurer.build();
        Pipeline pipeline = pipelineConfigurer.build();

        HttpClientEntityResponseHandler responseHandler = new HttpClientEntityResponseHandler(pipeline);
        return null;
    }
}
