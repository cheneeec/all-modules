package com.earnest.video.spider;

import com.earnest.crawler.core.Browser;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.builder.SpiderBuilder;
import com.earnest.crawler.core.spider.Spider;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public abstract class AbstractBaseVideoEntitySpider implements Spider, CommandLineRunner {

    protected final Spider spider;

    private final AtomicLong id = new AtomicLong(10000);

    public AbstractBaseVideoEntitySpider() {
        spider = createSpider();
    }

    private Spider createSpider() {
        return new SpiderBuilder()
                .global().userAgent(Browser.GOOGLE.userAgent()).setThreadNumber(3)
                .and()
                .request().method(Connection.Method.GET).from(getFromUrl())
                .and()
                .extract().range(getRangeRegexUrl())
                .and()
                .pipeline().cssSelector(getCssSelectorPipeline())
                .and()
                .build();
    }

    protected abstract Consumer<HttpResponseResult<Document>> getCssSelectorPipeline();


    protected abstract String getRangeRegexUrl();

    protected abstract String getFromUrl();

    protected Long generateId() {
        return id.getAndIncrement();
    }

    @Override
    public void start() {
        spider.start();
    }

    @Override
    public void stop() {
        spider.stop();
    }


    @Override
    public boolean isRunning() {
        return spider.isRunning();
    }

    @Override
    public void close() throws IOException {
        spider.close();
    }

    @Override
    public void run(String... args) throws Exception {
        spider.start();
    }
}
