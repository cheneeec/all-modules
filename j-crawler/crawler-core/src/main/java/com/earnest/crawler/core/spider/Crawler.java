package com.earnest.crawler.core.spider;


import com.earnest.crawler.core.StringResponseResult;
import com.earnest.crawler.core.downloader1.Downloader;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler1.Scheduler;
import lombok.AllArgsConstructor;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;


public class Crawler implements Closeable, Runnable {

    private final Downloader downloader;
    private final HttpRequestExtractor httpRequestExtractor;
    private final Scheduler scheduler;
    private final Pipeline pipeline;

    private volatile boolean running;

    public Crawler(Downloader downloader, HttpRequestExtractor httpRequestExtractor, Scheduler scheduler, Pipeline pipeline) {
        this.downloader = downloader;
        this.httpRequestExtractor = httpRequestExtractor;
        this.scheduler = scheduler;
        this.pipeline = pipeline;
    }

    @Override
    public void run() {

        while (running) {
            HttpUriRequest httpUriRequest = scheduler.take();
            StringResponseResult responseResult = downloader.download(httpUriRequest);
            Set<HttpUriRequest> httpUriRequests = httpRequestExtractor.extract(responseResult);
            scheduler.put(httpUriRequests);
            pipeline.pipe(responseResult);
        }

    }

    @Override
    public void close() throws IOException {
        downloader.close();
        close(httpRequestExtractor, scheduler, pipeline);
    }

    private void close(Object... o) throws IOException {
        for (Object o1 : o) {
            if (o1 instanceof Closeable) {
                ((Closeable) o1).close();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
