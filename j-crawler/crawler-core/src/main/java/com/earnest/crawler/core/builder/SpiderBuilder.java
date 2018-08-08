package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.downloader1.HttpClientDownloader;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler1.BlockingUniqueScheduler;
import com.earnest.crawler.core.scheduler1.Scheduler;
import com.earnest.crawler.core.spider.Crawler;
import com.earnest.crawler.core.spider.ExecutableSpider;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;


public class SpiderBuilder implements Builder<Spider> {

    //起点配置
    private final StartingPointConfigurer startingPointConfigurer;
    //全局的请求设置
    private final GlobalRequestConfigurer globalRequestConfigurer;
    //管道配置
    private final PipelineConfigurer pipelineConfigurer = new PipelineConfigurer(this);
    //新的请求提取器
    private final HttpUriRequestExtractorConfigurer httpUriRequestExtractorConfigurer = new HttpUriRequestExtractorConfigurer(this);
    //cookies存储器
    private final CookieStore cookieStore = new BasicCookieStore();
    //会话上下文
    private final HttpClientContext clientContext = new HttpClientContext();

    public SpiderBuilder() {
        this.startingPointConfigurer = new StartingPointConfigurer(this, cookieStore);
        this.globalRequestConfigurer = new GlobalRequestConfigurer(this, cookieStore);
        this.clientContext.setCookieStore(cookieStore);
    }


    //调度器



    /**
     * 设置开始爬取的起点网址。
     *
     * @return
     */
    public StartingPointConfigurer request() {
        return startingPointConfigurer;
    }

    /**
     * 设置全局的配置信息。
     *
     * @return
     */
    public GlobalRequestConfigurer global() {
        return globalRequestConfigurer;
    }

    /**
     * 设置管道处理器。
     *
     * @return
     */
    public PipelineConfigurer pipeline() {
        return pipelineConfigurer;
    }

    /**
     * 获取抓取的<code>url</code>配置。
     *
     * @return
     */
    public HttpUriRequestExtractorConfigurer extract() {
        return httpUriRequestExtractorConfigurer;
    }


    @Override
    public Spider build() {
        HttpUriRequest httpUriRequest = startingPointConfigurer.build();

        CloseableHttpClient closeableHttpClient = globalRequestConfigurer.build();

        HttpRequestExtractor httpRequestExtractor = httpUriRequestExtractorConfigurer.build();


        Pipeline pipeline = pipelineConfigurer.build();


        Scheduler scheduler = new BlockingUniqueScheduler();


        scheduler.put(httpUriRequest);

        return new ExecutableSpider(new Crawler(new HttpClientDownloader(closeableHttpClient, clientContext), httpRequestExtractor, scheduler, pipeline));
    }
}
