package com.earnest.crawler.core.extractor;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.request.HttpRequest;
import org.apache.commons.lang3.ObjectUtils;


import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractHttpRequestExtractor<T, R extends HttpResponseResult<T>> implements HttpRequestExtractor<T, R> {

    @Override
    public Set<HttpRequest> extract(R r) {
        HttpRequest httpRequest = r.getHttpRequest();

        return extractUrl(r).stream().map(url -> {
            HttpRequest cloneHttpRequest = ObjectUtils.cloneIfPossible(httpRequest);
            cloneHttpRequest.setUrl(url);
            return cloneHttpRequest;

        }).collect(Collectors.toSet());
    }

    protected abstract Set<String> extractUrl(R r);
}
