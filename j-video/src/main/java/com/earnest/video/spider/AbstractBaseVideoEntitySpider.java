package com.earnest.video.spider;

import com.earnest.crawler.core.builder.SpiderBuilder;
import com.earnest.crawler.core.spider.Spider;
import org.jsoup.Connection;
import org.springframework.http.MediaType;

import java.io.IOException;

public class AbstractBaseVideoEntitySpider<T> implements Spider {

    protected final Spider spider;

    public AbstractBaseVideoEntitySpider() {
        spider = createSpider();
    }

    private Spider createSpider() {



        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }


    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void close() throws IOException {

    }
}
