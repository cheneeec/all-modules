package com.earnest.video.spider;

import com.earnest.crawler.Browser;
import com.earnest.crawler.HttpResponseResult;
import com.earnest.crawler.builder.SpiderBuilder;
import com.earnest.crawler.Spider;
import com.earnest.video.entity.VideoEntity;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractBaseVideoEntitySpider implements Spider, CommandLineRunner {

    protected final Spider spider;


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

    protected Consumer<HttpResponseResult<Document>> getCssSelectorPipeline() {
        return documentHttpResponseResult -> {
            List<VideoEntity> videoEntities = pipe().apply(documentHttpResponseResult);
            if (!CollectionUtils.isEmpty(videoEntities)) {
                consumer().accept(videoEntities);
            }
        };
    }

    protected abstract Function<HttpResponseResult<Document>, List<VideoEntity>> pipe();

    protected abstract Consumer<List<? extends VideoEntity>> consumer();


    protected abstract String getRangeRegexUrl();

    protected abstract String getFromUrl();


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
    public void run(String... args) {
        start();
    }
}
