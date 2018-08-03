package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.crawler.Spider;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;


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

        return null;
    }
}
