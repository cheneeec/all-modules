package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;

import java.util.List;
import java.util.Set;

@FunctionalInterface
public interface HttpResponseHandler {
    Set<HttpRequest> handle(HttpResponse rawResponse);
}
