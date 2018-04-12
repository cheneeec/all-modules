package com.earnest.crawler.core.parser;

import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//TODO 不好用。需要定义一大堆注解，也会让类变得很乱。
public class ClassAnnotatedParser implements Parser {

    private HttpResponseHandler httpResponseHandler;
    private Pipeline<?> pipeline;
    private Set<HttpRequest> httpRequests = new HashSet<>(6);
    private Set<?> persistenceConsumers;
    private int threadNumber;

    public ClassAnnotatedParser(Class<?> clazz) {

    }

    @Override
    public HttpResponseHandler getHttpResponseHandler() {
        return null;
    }

    @Override
    public <T> Pipeline<T> getPipeline() {
        return null;
    }

    @Override
    public Set<HttpRequest> getHttpRequests() {
        return null;
    }

    @Override
    public <T> Set<T> getPersistenceConsumers() {
        return null;
    }

    @Override
    public int getThread() {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
