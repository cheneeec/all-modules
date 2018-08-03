package com.earnest.crawler.core.extractor;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.request.HttpRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;


import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractHttpRequestExtractor implements HttpRequestExtractor {


    @Override
    public Set<HttpUriRequest> extract(HttpResponseResult<String> responseResult) {
        HttpUriRequest httpRequest = responseResult.getHttpRequest();

        RequestBuilder requestBuilder = RequestBuilder.copy(httpRequest);

        return extractUrl(responseResult).stream().map(url ->
                requestBuilder.setUri(url).build()
        ).collect(Collectors.toSet());
    }


    protected abstract Set<String> extractUrl(HttpResponseResult<String> responseResult);
}
