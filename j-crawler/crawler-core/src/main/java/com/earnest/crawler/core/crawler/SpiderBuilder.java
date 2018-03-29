package com.earnest.crawler.core.crawler;

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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static java.util.Collections.singleton;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("unchecked")
public class SpiderBuilder {

    private Scheduler scheduler;

    private HttpResponseHandler responseHandler;

    private Downloader downloader;

    private Pipeline<?> pipeline;

    private Set<Consumer<?>> persistenceConsumers;

    private Set<DownloadListener> downloadListeners;


    private int threadNumber = 1;


    public static SpiderBuilder create() {
        return new SpiderBuilder();
    }

    public SpiderBuilder thread(int num) {
        threadNumber = num;
        //设置最大连接数，系统默认是5x2
        int maxConnectionCount = (int) Math.ceil(((double) num / 2));
        System.getProperties().setProperty("http.maxConnections", String.valueOf(maxConnectionCount));
        log.info("set SystemProperty value: [http.maxConnections={}]", maxConnectionCount * 2);
        return this;
    }

    public SpiderBuilder from(String url) {
        return from(new HttpGetRequest(url));
    }

    public SpiderBuilder from(HttpRequest httpRequest) {
        scheduler = new BlockingQueueScheduler();
        scheduler.offer(httpRequest);
        //

        return this;
    }

    public SpiderBuilder addRequest(HttpRequest httpRequest) {
        if (isNull(scheduler)) {
            return from(httpRequest);
        } else {
            scheduler.offer(httpRequest);
        }
        return this;
    }

    public SpiderBuilder downloader(Downloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public SpiderBuilder addDownloaderListener(DownloadListener downloadListener) {
        if (nonNull(downloadListener)) {
            if (isNull(downloadListeners)) {
                downloadListeners = new HashSet<>(5);
            }
            downloadListeners.add(downloadListener);
        }
        return this;
    }


    public <T> Spider build() {
        Crawler crawler = this.<T>createCrawler();
        SpiderSetter spider = new BasicSpider();

        spider.<T>setCrawler(crawler);
        spider.setThread(threadNumber);

        return spider;
    }


    private <T> Crawler createCrawler() {
        Assert.state(nonNull(scheduler), "The URL that started crawling is not set");
        Crawler crawler = new BasicCrawler<T>();
        //set Downloader
        crawler.setDownloader(decideDownloader());
        //--set Downloader

        crawler.setPipeline(defaultIfNull(pipeline, (Pipeline<T>) httpResponse -> (T) httpResponse));

        crawler.setPersistenceConsumers(defaultIfNull(persistenceConsumers, singleton((Consumer<T>) System.out::println)));

        crawler.setScheduler(scheduler);
        //set HttpResponseHandler
        crawler.setHttpResponseHandler(decideHttpResponseHandler());
        //--set HttpResponseHandler

        return crawler;
    }

    private HttpResponseHandler decideHttpResponseHandler() {
        return ObjectUtils.defaultIfNull(responseHandler, httpResponse -> Collections.emptySet());
    }

    private Downloader decideDownloader() {

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
        return defaultDownloader;
    }

    public SpiderBuilder match(String regex) {
        responseHandler = new RegexHttpResponseHandler(regex);
        return this;
    }

    public SpiderBuilder httpResponseHandler(HttpResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
        return this;
    }


    public SpiderBuilder pipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    public <T> SpiderBuilder addConsumer(Consumer<T> persistenceConsumer) {
        if (nonNull(persistenceConsumer)) {
            if (isNull(persistenceConsumers)) {
                persistenceConsumers = new HashSet<>(2);
            }
            persistenceConsumers.add(persistenceConsumer);
        }
        return this;
    }
}
