package com.earnest.crawler.core.worker;


import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.downloader.HttpClientDownloader;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.handler.RegexHttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.scheduler.BlockingQueueScheduler;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Slf4j
public class Spider extends Worker implements ISpider {

    private ExecutorService threadPool;
    private Scheduler scheduler;

    private HttpResponseHandler responseHandler;
    private Downloader downloader;
    private Pipeline<?> pipeline;

    private Set<Consumer<?>> persistenceConsumers;

    private Worker worker;

    @Override
    public void pause() {

    }

    @Override
    public boolean isPause() {
        return false;
    }

    @Override
    public void restart() {

    }


    @Override
    public ISpider thread(int num) {
        threadPool = Executors.newFixedThreadPool(num);
        return this;
    }

    @Override
    public ISpider from(String url) {
        return from(new HttpGetRequest(url));
    }

    @Override
    public ISpider from(HttpRequest httpRequest) {
        scheduler = new BlockingQueueScheduler();
        scheduler.offer(httpRequest);
        return this;
    }

    @Override
    public ISpider addRequest(HttpRequest httpRequest) {
        if (Objects.isNull(scheduler)) {
            return from(httpRequest);
        } else {
            scheduler.offer(httpRequest);
        }
        return this;
    }

    @Override
    public ISpider downloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    @Override
    public <T> ISpider start() {
        this.<T>createWork();

        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> void createWork() {
        Assert.state(Objects.nonNull(scheduler), "The URL that started crawling is not set");
        worker = new Worker<T>();
        worker.setDownloader(defaultIfNull(downloader, new HttpClientDownloader()));
        worker.setPipeline(defaultIfNull(pipeline, (Pipeline<?>) (entity) -> entity));
        worker.setPersistenceConsumers(defaultIfNull(persistenceConsumers, Collections.singleton((Consumer<T>) System.out::println)));
        worker.setScheduler(scheduler);
        if (Objects.isNull(responseHandler)) {
            //只会爬取一页
            worker.setResponseHandler(httpResponse -> Collections.emptyList());
            log.warn("Since {} is not set, only {} will be downloaded", HttpResponseHandler.class, JSONObject.toJSONString(scheduler));
        } else {
            worker.setResponseHandler(responseHandler);
        }
    }

    @Override
    public ISpider match(String regex) {
        responseHandler = new RegexHttpResponseHandler(regex);
        return this;
    }

    @Override
    public ISpider stop() {

        return this;
    }

    @Override
    public <T> ISpider pipeline(Pipeline<T> pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    @Override
    public <T> ISpider addConsumer(Consumer<T> persistenceConsumer) {
        return null;
    }
}
