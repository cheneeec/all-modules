package com.earnest.crawler.core.spider;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.handler1.HttpClientResponseContextHolder;
import com.earnest.crawler.core.handler2.StringHttpClientResponseHandler;
import com.earnest.crawler.core.scheduler1.Scheduler;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Setter
public abstract class AbstractSpider implements Spider, Runnable {

    protected CloseableHttpClient httpClient;

    protected HttpContext httpContext;

    protected CookieStore cookieStore;

    protected Scheduler scheduler;

    protected ExecutorService threadPool;

    protected StringHttpClientResponseHandler stringHttpClientResponseHandler = new StringHttpClientResponseHandler();


    protected HttpRequestExtractor requestExtractor;

    private int thread = 5;


    @Override
    public void start() {
        threadPool = Executors.newFixedThreadPool(thread);
        for (int i = 0; i < thread; i++) {
            threadPool.execute(this);
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void run() {
        while (!scheduler.isEmpty()) {
            try {
                //
                HttpUriRequest request = scheduler.take();

                //download
                CloseableHttpResponse closeableHttpResponse = httpClient.execute(request, httpContext);

                HttpClientResponseContextHolder.setHttpUriRequest(closeableHttpResponse,request);
                //
                HttpResponseResult<String> stringHttpResponseResult = stringHttpClientResponseHandler.handleResponse(closeableHttpResponse);

                if (cookieStore != null) {
                    Map<String, String> cookies = stringHttpResponseResult.getCookies();
                    cookieStore.getCookies().forEach(c -> cookies.put(c.getName(), c.getValue()));
                }


                if (ObjectUtils.allNotNull(scheduler, requestExtractor)) {
                    //add new request
                    requestExtractor.extract(stringHttpResponseResult).forEach(scheduler::put);
                }
                handle(stringHttpResponseResult);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    protected abstract void handle(HttpResponseResult<String> stringHttpResponseResult) throws IOException;

}
