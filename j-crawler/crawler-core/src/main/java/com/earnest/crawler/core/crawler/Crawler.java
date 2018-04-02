package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.scheduler.Scheduler;

import java.util.Set;
import java.util.function.Consumer;

public interface Crawler<T> extends Runnable {
    String getName();

    void setName(String name);

    void setScheduler(Scheduler scheduler);

    void setPipeline(Pipeline<T> pipeline);

    void setHttpResponseHandler(HttpResponseHandler httpResponseHandler);

    void setDownloader(Downloader downloader);

    void setPersistenceConsumers(Set<Consumer<T>> persistenceConsumers);

    Scheduler getScheduler();

    Pipeline<T> getPipeline();

    HttpResponseHandler getHttpResponseHandler();

    Downloader getDownloader();

    Set<Consumer<T>> getPersistenceConsumers();

    void destroy();

}
