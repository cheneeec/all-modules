package com.earnest.crawler.core.handler1;

import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.scheduler.Scheduler;
import lombok.AllArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

@AllArgsConstructor
public class HttpClientResponseHandler extends BasicResponseHandler {

    private final Scheduler scheduler;

    private final HttpResponseHandler httpResponseHandler;


    @Override
    public String handleEntity(HttpEntity entity) throws IOException {

        return super.handleEntity(entity);
    }

    @Override
    public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {

        return super.handleResponse(response);
    }
}
