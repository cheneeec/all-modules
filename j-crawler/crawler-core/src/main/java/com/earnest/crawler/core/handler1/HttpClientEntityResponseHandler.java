package com.earnest.crawler.core.handler1;


import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.builder.Type;
import com.earnest.crawler.core.pipeline.HtmlDocumentPipeline;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AllArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.AbstractResponseHandler;

import java.io.IOException;
import java.util.List;


@AllArgsConstructor
public class HttpClientEntityResponseHandler<T> extends AbstractResponseHandler<T> {

    private final Scheduler scheduler;

    private final Type type;

    private final HtmlDocumentPipeline<List<T>> pipeline;


    @Override
    public T handleResponse(HttpResponse response) throws HttpResponseException, IOException {

        return super.handleResponse(response);
    }

    @Override
    public T handleEntity(HttpEntity entity) throws IOException {
        return null;
    }

    private HttpResponseResult convertHttpResponseToResult(HttpResponse response, Type type) {

        return null;
    }
}
