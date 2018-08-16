package com.earnest.video.spider;

import com.earnest.crawler.core.spider.Spider;

import java.io.IOException;
import java.util.List;

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
    public void close() throws IOException {

    }
}
