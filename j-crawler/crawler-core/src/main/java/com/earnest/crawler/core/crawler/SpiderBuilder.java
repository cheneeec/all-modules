package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.MultiThreadBean;
import com.earnest.crawler.core.downloader.*;
import com.earnest.crawler.core.downloader.listener.DownloadListener;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.handler.RegexHttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import com.earnest.crawler.core.scheduler.BlockingUniqueScheduler;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

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

    private Predicate<HttpResponse> stopPredicate;

    private int threadNumber = 1;


    public static SpiderBuilder create() {
        return new SpiderBuilder();
    }

    public SpiderBuilder thread(int num) {
        Assert.isTrue(num > 0, "num must be greater than 0");
        threadNumber = num;

        return this;
    }

    public SpiderBuilder from(String url) {
        return from(new HttpGetRequest(url));
    }

    public SpiderBuilder from(HttpRequest httpRequest) {
        scheduler = new BlockingUniqueScheduler();
        scheduler.put(httpRequest);
        //

        return this;
    }

    public SpiderBuilder addRequest(HttpRequest httpRequest) {
        if (isNull(scheduler)) {
            return from(httpRequest);
        } else {
            scheduler.put(httpRequest);
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
        BasicSpider spider = new DefaultSpider();

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

        crawler.setStopWhen(stopPredicate);

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

        if (nonNull(scheduler)) {
            if (!CollectionUtils.isEmpty(downloadListeners)) {
                downloadListeners.add( scheduler);
            } else {
                downloadListeners = Collections.singleton(scheduler);
            }
        }

        if (!CollectionUtils.isEmpty(downloadListeners)) {
            if (defaultDownloader instanceof HttpClientDownloader) {
                downloadListeners.forEach(((HttpClientDownloader) defaultDownloader)::addDownloadListener);
            }
        }
        if (threadNumber > 1) {
            if (defaultDownloader instanceof MultiThreadBean) {
                ((MultiThreadBean) defaultDownloader).setThread(threadNumber);
            } else {
                throw new IllegalStateException(String.format("cannot set the threadNumber for %s ,this class must implement %s",
                        defaultDownloader.getClass(), MultiThreadBean.class));
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

    public SpiderBuilder stopWhen(Predicate<HttpResponse> stopPredicate) {
        this.stopPredicate = stopPredicate;
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
