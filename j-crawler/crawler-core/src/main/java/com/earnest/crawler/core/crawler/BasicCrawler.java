package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.crawler.listener.StopListener;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.event.CrawlerStopEvent;
import com.earnest.crawler.core.exception.TakeTimeoutException;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import com.earnest.crawler.core.scheduler.BlockingScheduler;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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

    private volatile boolean running;
    private volatile boolean closed;

    private final Set<StopListener> stopListeners = new HashSet<>();
    private final AtomicBoolean ranStopListeners = new AtomicBoolean();


    @Override
    public void run() {
        log.info("the crawler:[{}] is running", getName());
        running = true;
        while (running && !closed) {
            //1. 获取连接
            HttpRequest httpRequest = takeHttpRequest();
            if (nonNull(httpRequest)) {
                HttpResponse httpResponse = downloader.download(httpRequest);
                //2. 处理HttpResponse并且获取新的连接
                Set<HttpRequest> newHttpRequests = httpResponseHandler.handle(httpResponse);
                //3. 将新的连接放入
                scheduler.addAll(newHttpRequests);

                //4. 将httpResponse转化成实体类
                T pipeResult = pipeline.pipe(httpResponse);
                //5. 将结果进行消化
                persistenceConsumers.forEach(a -> a.accept(pipeResult));
                //退出条件(只应该运行一次)
                if (nonNull(stopWhen) && ranStopListeners.get()&& stopWhen.test(httpResponse)) {
                    callStopListeners(httpRequest);
                    break;
                }
            } else
                break;
        }
    }

    private  void callStopListeners(HttpRequest httpRequest) {
        if (!ranStopListeners.getAndSet(true)) {
            CrawlerStopEvent event = new CrawlerStopEvent(httpRequest);
            stopListeners.forEach(stopListener -> stopListener.onStop(event));
            running = false;
        }
    }

    private HttpRequest takeHttpRequest() {
        HttpRequest httpRequest = null;
        try {
            if (scheduler instanceof BlockingScheduler)
                //默认一分钟拿不到，就抛出TakeTimeoutException
                httpRequest = ((BlockingScheduler) scheduler).take(60, TimeUnit.SECONDS);
            else
                httpRequest = scheduler.take();
        } catch (InterruptedException e) {
            log.error("An error:[{}] occurred while getting a httpRequest", e.getMessage());
        } catch (TakeTimeoutException e) {
            log.info("Waiting time is too long, crawler:[{}] is about to stop", getName());
            callStopListeners(new HttpGetRequest());
        }
        return httpRequest;
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
    public Set<StopListener> getStopListeners() {
        return stopListeners;
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
            closed = true;
        }
    }

}
