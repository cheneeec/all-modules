package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class BasicCrawler<T> implements Crawler<T> {

    private Scheduler scheduler;
    private Pipeline<T> pipeline;
    private HttpResponseHandler httpResponseHandler;
    private Downloader downloader;
    private Set<Consumer<T>> persistenceConsumers;

    private final String defaultName = Thread.currentThread().getName();

    private String name;

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        log.info("{} is running", getName());
        while (!scheduler.isEmpty()) {
            //暂停

            //1. 获取连接
            HttpRequest httpRequest = this.scheduler.poll();
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
            }
        }
    }

    @Override
    public String getName() {
        return StringUtils.defaultString(name, defaultName);
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
}
