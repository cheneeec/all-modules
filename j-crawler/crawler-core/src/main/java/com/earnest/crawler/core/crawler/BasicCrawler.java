package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.crawler.listener.StopListener;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.event.CrawlerStopEvent;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Objects.nonNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class BasicCrawler<T> implements Crawler<T> {

    private Scheduler scheduler;
    private Pipeline<T> pipeline;
    private HttpResponseHandler httpResponseHandler;
    private Downloader downloader;
    private Set<Consumer<T>> persistenceConsumers;
    private Predicate<HttpResponse> stopWhen;

    private String name;

    private List<StopListener> stopListeners = new ArrayList<>();

    @Override
    public void run() {
        log.info("start isRunning,name={}", getName());
        while (!Thread.currentThread().isInterrupted()) {
            //暂停

            //1. 获取连接
            HttpRequest httpRequest = this.scheduler.take();
            if (nonNull(httpRequest)) {
                HttpResponse httpResponse = downloader.download(httpRequest);
                //2. 处理HttpResponse并且获取新的连接
                Set<HttpRequest> newHttpRequests = httpResponseHandler.handle(httpResponse);
                //3. 将新的连接放入
                scheduler.addAll(newHttpRequests);

                //4. 将httpResponse转化成实体类
                T pipeResult = pipeline.pipe(httpResponse);
                //5. 将结果进行消化
                persistenceConsumers.parallelStream().forEach(a -> a.accept(pipeResult));
                //退出条件
                if (nonNull(stopWhen) && stopWhen.test(httpResponse)) {
                    CrawlerStopEvent event = new CrawlerStopEvent(httpRequest);
                    stopListeners.forEach(stopListener -> stopListener.onStop(event));
                    break;
                }
            } else {

                break;
            }
        }
    }

    @Override
    public String getName() {
        return StringUtils.defaultString(name, String.valueOf(Thread.currentThread().getName()));
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void setPipeline(Pipeline<T> pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void setHttpResponseHandler(HttpResponseHandler httpResponseHandler) {
        this.httpResponseHandler = httpResponseHandler;
    }

    @Override
    public void setDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public void setPersistenceConsumers(Set<Consumer<T>> persistenceConsumers) {
        this.persistenceConsumers = persistenceConsumers;
    }

    @Override
    public void setStopWhen(Predicate<HttpResponse> stopPredicate) {
        this.stopWhen = stopPredicate;
    }

    @Override
    public void addStopListener(StopListener stopListener) {
        this.stopListeners.add(stopListener);
        ;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public Pipeline<T> getPipeline() {
        return pipeline;
    }

    @Override
    public HttpResponseHandler getHttpResponseHandler() {
        return httpResponseHandler;
    }

    @Override
    public Downloader getDownloader() {
        return downloader;
    }


    @Override
    public Set<Consumer<T>> getPersistenceConsumers() {
        return persistenceConsumers;
    }

    @Override
    public Predicate<HttpResponse> getStopWhen() {
        return stopWhen;
    }


    @Override
    public void close() {
        try {
            downloader.close();

        } catch (IOException e) {
            log.error("An error occurred while invoking destroy,error:" + e.getMessage());
        } finally {
            try {
                downloader.close();
            } catch (IOException ignore) {
            }
        }
    }
}
