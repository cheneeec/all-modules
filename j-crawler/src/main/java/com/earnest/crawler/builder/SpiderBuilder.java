package com.earnest.crawler.builder;

import com.earnest.crawler.Spider;
import com.earnest.crawler.downloader.Downloader;
import com.earnest.crawler.extractor.HttpRequestExtractor;
import com.earnest.crawler.pipeline.Pipeline;
import com.earnest.crawler.scheduler.Scheduler;
import com.earnest.crawler.AsyncSpider;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SpiderBuilder extends SharedSpiderConfigurer implements Builder<Spider> {


    private final Map<Class<? extends SharedSpiderConfigurer>, SharedSpiderConfigurer> configurers = new LinkedHashMap<>();


    private final List<SharedSpiderConfigurer> sharedSpiderConfigurers;

    public SpiderBuilder() {
        init();
        this.sharedSpiderConfigurers = createSharedSpiderConfigurers();
    }

    private List<SharedSpiderConfigurer> createSharedSpiderConfigurers() {
        return Stream.of(new HttpUriRequestConfigurer(), new DownloaderConfigurer(), new PipelineConfigurer(), new HttpUriRequestExtractorConfigurer(), new SchedulerConfigurer())
                .sorted(Comparator.comparingInt(SharedSpiderConfigurer::order))
                .peek(e -> {
                    e.setBuilder(this);
                    e.setSharedObjectMap(sharedObjectMap);
                    configurers.put(e.getClass(), e);
                    e.init();
                })
                .collect(Collectors.toList());
    }

    @Override
    void init() {
        sharedObjectMap = new LinkedHashMap<>();
    }


    //调度器


    /**
     * 设置开始爬取的起点网址。
     *
     * @return
     */
    public HttpUriRequestConfigurer request() {
        return (HttpUriRequestConfigurer) configurers.get(HttpUriRequestConfigurer.class);
    }

    /**
     * 设置全局的配置信息。
     *
     * @return
     */
    public DownloaderConfigurer global() {
        return (DownloaderConfigurer) configurers.get(DownloaderConfigurer.class);
    }

    /**
     * 设置管道处理器。
     *
     * @return
     */
    public PipelineConfigurer pipeline() {
        return (PipelineConfigurer) configurers.get(PipelineConfigurer.class);
    }

    /**
     * 获取抓取的<code>url</code>配置。
     *
     * @return
     */
    public HttpUriRequestExtractorConfigurer extract() {
        return (HttpUriRequestExtractorConfigurer) configurers.get(HttpUriRequestExtractorConfigurer.class);
    }

    /**
     * 设置调度器的类型。
     *
     * @return
     */
    public SchedulerConfigurer scheduler() {
        return (SchedulerConfigurer) configurers.get(SchedulerConfigurer.class);
    }

    @Override
    void configure() {
        sharedSpiderConfigurers.forEach(SharedSpiderConfigurer::configure);
    }

    @Override
    public Spider build() {

        configure();

        //downloader
        Downloader downloader = (Downloader) sharedObjectMap.get(Downloader.class).get(0);
        //httpRequestExtractor
        HttpRequestExtractor httpRequestExtractor = (HttpRequestExtractor) sharedObjectMap.get(HttpRequestExtractor.class).get(0);
        //scheduler
        Scheduler scheduler = (Scheduler) sharedObjectMap.get(Scheduler.class).get(0);
        //custom
        Pipeline pipeline = (Pipeline) sharedObjectMap.get(Pipeline.class).get(0);
        //thread
        Integer thread = (Integer) sharedObjectMap.get(Integer.class).get(0);


        return new AsyncSpider(downloader, scheduler, httpRequestExtractor, pipeline, thread);
    }

}
