package com.earnest.crawler.core.extractor;


import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.request.HttpRequest;

import java.util.Set;

@FunctionalInterface
public interface HttpRequestExtractor<T, R extends HttpResponseResult<T>> {

    Set<HttpRequest> extract(R r);

}
