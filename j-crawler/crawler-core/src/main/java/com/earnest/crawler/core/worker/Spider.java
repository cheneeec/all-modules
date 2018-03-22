package com.earnest.crawler.core.worker;


import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.core.downloader.DownloadListener;
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
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Slf4j
public class Spider extends Worker implements ISpider {

    private Scheduler scheduler;

    private HttpResponseHandler responseHandler;
    private Downloader downloader;
    private Pipeline<?> pipeline;

    private Set<Consumer<?>> persistenceConsumers;

    private Set<DownloadListener> downloadListeners;

    private Worker worker;

    private int threadNumber = 1;

   /* @Override
    public void pause() {

    }

    @Override
    public boolean isPause() {
        return false;
    }

    @Override
    public void restart() {

    }
*/

    @Override
    public ISpider thread(int num) {
        threadNumber = num;
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
        //

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
    public ISpider addDownloaderListener(DownloadListener downloadListener) {
        if (nonNull(downloadListener)) {
            if (Objects.isNull(downloadListeners)) {
                downloadListeners = new HashSet<>(5);
            }
            downloadListeners.add(downloadListener);
        }
        return this;
    }

    @Override
    public <T> ISpider start() {
        Worker worker = this.<T>createWorker();

        Integer i = threadNumber;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadNumber);

        while (i > 0) {
            Thread thread = new Thread(worker);
            threadPool.execute(thread);
            i--;
            log.info("Thread {} is turned on", thread.getName());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    private <T> Worker createWorker() {
        Assert.state(nonNull(scheduler), "The URL that started crawling is not set");
        worker = new Worker<T>();
        //set Downloader
        decideDownloader();
        //--set Downloader

        worker.setPipeline(defaultIfNull(pipeline, (Pipeline<?>) (entity) -> entity));

        worker.setPersistenceConsumers(defaultIfNull(persistenceConsumers, Collections.singleton((Consumer<T>) System.out::println)));

        worker.setScheduler(scheduler);
        //set HttpResponseHandler
        decideHttpResponseHandler();
        //--set HttpResponseHandler

        return worker;
    }

    private void decideHttpResponseHandler() {
        if (Objects.isNull(responseHandler)) {
            //只会爬取一页
            worker.setResponseHandler(httpResponse -> Collections.emptySet());
            log.warn("Since {} is not set, only {} will be downloaded", HttpResponseHandler.class, JSONObject.toJSONString(scheduler));
        } else {
            worker.setResponseHandler(responseHandler);
        }
    }

    private void decideDownloader() {
        Downloader defaultDownloader = defaultIfNull(this.downloader, new HttpClientDownloader());


        if (scheduler instanceof DownloadListener) {
            ConcurrentHashMap.KeySetView<DownloadListener, Boolean> downloadListenersSet = ConcurrentHashMap.newKeySet();
            downloadListenersSet.add(((DownloadListener) scheduler));
            downloadListeners = Optional.ofNullable(downloadListeners)
                    .orElse(downloadListenersSet);
        }

        if (!CollectionUtils.isEmpty(downloadListeners)) {
            if (defaultDownloader instanceof HttpClientDownloader) {
                downloadListeners.forEach(((HttpClientDownloader) defaultDownloader)::addDownloadListener);
            }
        }


        worker.setDownloader(defaultDownloader);
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
        if (nonNull(persistenceConsumer)) {
            if (Objects.isNull(persistenceConsumers)) {
                persistenceConsumers = new HashSet<>(2);
            }
            persistenceConsumers.add(persistenceConsumer);

        }
        return this;
    }
}
