package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.PageResponse;

import java.util.Set;

@FunctionalInterface
public interface HttpResponseHandler {
    Set<HttpRequest> handle(PageResponse rawResponse);
}
