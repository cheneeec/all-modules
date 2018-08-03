package com.earnest.crawler.core.spider;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Setter
public class SingleDomainSpider implements Spider, Runnable {

    private final CloseableHttpClient httpClient;
    private final HttpContext httpContext;
    private final CookieStore cookieStore = new BasicCookieStore();
    private int thread;

    private final Scheduler scheduler;


    @Override
    public void start() {

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

    }
}
