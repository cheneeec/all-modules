package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;

import java.util.List;

@FunctionalInterface
public interface HttpResponseHandler {
    List<HttpRequest> handle(HttpResponse rawResponse);
}
