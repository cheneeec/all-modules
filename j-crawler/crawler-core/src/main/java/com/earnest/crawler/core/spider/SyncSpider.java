package com.earnest.crawler.core.spider;

import com.alibaba.fastjson.util.IOUtils;
import com.earnest.crawler.core.StringResponseResult;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.exception.TakeTimeoutException;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler.Scheduler;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.Assert;

import java.io.Closeable;
import java.util.Arrays;
import java.util.Set;

public class SyncSpider implements Spider {

    protected final Downloader downloader;
    protected final Scheduler scheduler;
    protected final HttpRequestExtractor httpRequestExtractor;
    protected final Pipeline pipeline;

    public SyncSpider(Downloader downloader, Scheduler scheduler, HttpRequestExtractor httpRequestExtractor, Pipeline pipeline) {
        Assert.notNull(downloader, "downloader is null");
        Assert.notNull(scheduler, "scheduler is null");
        Assert.notNull(pipeline, "pipeline is null");
        this.downloader = downloader;
        this.scheduler = scheduler;
        this.httpRequestExtractor = httpRequestExtractor;
        this.pipeline = pipeline;
    }

    @Override
    public void start() {

        while (true) {
            HttpUriRequest httpUriRequest;
            try {
                httpUriRequest = scheduler.take();
            } catch (TakeTimeoutException e) { //对于阻塞的调度器发出的异常
                break;
            }
            if (httpUriRequest == null) {  //取出null值时
                break;
            }
            StringResponseResult stringResponseResult = downloader.download(httpUriRequest);
            stringResponseResultHandle(stringResponseResult);

        }

    }

    /**
     * 对{@link StringResponseResult}进行处理。
     *
     * @param stringResponseResult
     */
    protected void stringResponseResultHandle(StringResponseResult stringResponseResult) {
        //提取新的链接
        Set<HttpUriRequest> httpUriRequests = httpRequestExtractor.extract(stringResponseResult);
        //将结果放入
        scheduler.putAll(httpUriRequests);
        //将结果进行处理
        pipeline.pipe(stringResponseResult);
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("SyncSpider can't stop");
    }

    @Override
    public boolean isRunning() {
        return Thread.currentThread().isAlive();
    }

    @Override
    public void close() {
        closeComponents(downloader, scheduler, httpRequestExtractor, pipeline);
    }


    void closeComponents(Object... components) {
        Arrays.stream(components)
                .filter(a -> a instanceof Closeable)
                .map(s -> (Closeable) s)
                .forEach(IOUtils::close);
    }
}
