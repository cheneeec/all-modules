package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.downloader1.Downloader;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler1.Scheduler;
import com.earnest.crawler.core.spider.Crawler;
import com.earnest.crawler.core.spider.ExecutableSpider;

import java.util.*;


public class SpiderBuilder implements Builder<Spider> {


    private final Map<Class<? extends SharedSpiderConfigurer>, SharedSpiderConfigurer> configurers = new LinkedHashMap<>();

    private Map<Class<?>, List<?>> sharedObjectMap = new LinkedHashMap<>();

    private final List<SharedSpiderConfigurer> sharedSpiderConfigurers = new ArrayList<>();

    public SpiderBuilder() {
        init();
    }

    private void init() {

        sharedSpiderConfigurers.add(new HttpUriRequestConfigurer());
        sharedSpiderConfigurers.add(new DownloaderConfigurer());
        sharedSpiderConfigurers.add(new PipelineConfigurer());
        sharedSpiderConfigurers.add(new HttpUriRequestExtractorConfigurer());
        sharedSpiderConfigurers.add(new SchedulerConfigurer());

        Collections.sort(sharedSpiderConfigurers);


        sharedSpiderConfigurers.forEach(e -> {
            e.setBuilder(this);
            e.setSharedObjectMap(sharedObjectMap);
            configurers.put(e.getClass(), e);
            e.init();
        });

      /*  configurers.put(HttpUriRequestConfigurer.class, new HttpUriRequestConfigurer());
        //全局的请求设置
        configurers.put(DownloaderConfigurer.class, new DownloaderConfigurer());
        //管道配置
        configurers.put(PipelineConfigurer.class, new PipelineConfigurer());
        //新的请求提取器
        configurers.put(HttpUriRequestExtractorConfigurer.class, new HttpUriRequestExtractorConfigurer());*/


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

    public SchedulerConfigurer scheduler() {
        return (SchedulerConfigurer) configurers.get(SchedulerConfigurer.class);
    }


    @Override
    public Spider build() {
        sharedSpiderConfigurers.forEach(SharedSpiderConfigurer::configure);
        //downloader
        Downloader downloader = (Downloader) sharedObjectMap.get(Downloader.class).get(0);
        //httpRequestExtractor
        HttpRequestExtractor httpRequestExtractor = (HttpRequestExtractor) sharedObjectMap.get(HttpRequestExtractor.class).get(0);
        //scheduler
        Scheduler scheduler = (Scheduler) sharedObjectMap.get(Scheduler.class).get(0);
        //pipeline
        Pipeline pipeline = (Pipeline) sharedObjectMap.get(Pipeline.class).get(0);
        //thread
        Integer thread = (Integer) sharedObjectMap.get(Integer.class).get(0);

        Crawler crawler = new Crawler(downloader, httpRequestExtractor, scheduler, pipeline);

        crawler.run();

        return new ExecutableSpider(crawler);
    }
}
