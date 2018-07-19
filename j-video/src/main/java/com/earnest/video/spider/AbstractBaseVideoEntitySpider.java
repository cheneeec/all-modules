package com.earnest.video.spider;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.crawler.SpiderBuilder;
import com.earnest.crawler.core.crawler.Spiders;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.video.entity.BaseVideoEntity;
import org.apache.commons.lang3.reflect.ConstructorUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class AbstractBaseVideoEntitySpider<T extends BaseVideoEntity> implements Spider {

    private final Spider spider;

    protected final AtomicLong atomicLong = new AtomicLong();

    private final Class<T> clazz;

    protected AbstractBaseVideoEntitySpider(String fromUrl, String match, int threadNumber) {
        this(new HttpGetRequest(fromUrl), match, threadNumber);
    }

    protected AbstractBaseVideoEntitySpider(String fromUrl, String match) {
        this(fromUrl, match, 5);
    }

    protected AbstractBaseVideoEntitySpider(HttpRequest httpRequest, String match, int threadNumber) {
        spider = createSpider(httpRequest, match, threadNumber);
        clazz = getGenericClass();
    }


    private Class<T> getGenericClass() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private Spider createSpider(HttpRequest fromUrl, String match, int threadNumber) {
        Spider spider;
        SpiderBuilder spiderBuilder = createSpiderBuilder(createPipeline(), fromUrl, match, threadNumber).addConsumer(getSpiderConsumer());
        additionalSpiderSetting(spiderBuilder);
        spider = spiderBuilder.build();
        return spider;
    }

    protected Consumer<List<T>> getSpiderConsumer() {
        return null;
    }

    protected void additionalSpiderSetting(SpiderBuilder spiderBuilder) {}


    protected abstract Pipeline<List<T>> createPipeline();

    protected T newBaseVideoEntity(HttpRequest httpRequest) {
        try {
            T t = ConstructorUtils.invokeConstructor(clazz);
            t.setFromUrl(httpRequest.getUrl());
            t.setCollectTime(new Date());
            t.setId(atomicLong.incrementAndGet());
            return t;
        } catch (Exception e) {
            throw new IllegalStateException("Creating this object is not supported,error:" + e.getMessage());
        }

    }

    private SpiderBuilder createSpiderBuilder(Pipeline<List<T>> pipeline, HttpRequest from, String match, int threadNumber) {
        return Spiders.createCustom()
                .from(from)
                .match(match)
                .pipeline(pipeline)
                .thread(threadNumber);

    }

    @Override
    public void start() {
        spider.start();
    }

    @Override
    public void shutdown() {
        spider.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return spider.shutdownNow();
    }

    @Override
    public boolean isRunning() {
        return spider.isRunning();
    }

    @Override
    public String getName() {
        return spider.getName();
    }

    @Override
    public void setName(String name) {
        this.spider.setName(name);
    }

    @Override
    public void close() throws IOException {
        spider.close();
    }

}
