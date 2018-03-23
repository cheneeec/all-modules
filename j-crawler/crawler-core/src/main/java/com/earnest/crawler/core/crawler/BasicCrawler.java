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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

@Slf4j
@Setter(AccessLevel.PROTECTED)
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
class BasicCrawler<T> implements  Runnable {

    private Scheduler scheduler;
    private Pipeline<T> pipeline;
    private HttpResponseHandler httpResponseHandler;
    private Downloader downloader;
    private Set<Consumer<T>> persistenceConsumers;

    private static final String NAME = Thread.currentThread().getName();


    @Override
    @SuppressWarnings("unchecked")
    public  void run() {
        log.info("{} is running", NAME);
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
}
