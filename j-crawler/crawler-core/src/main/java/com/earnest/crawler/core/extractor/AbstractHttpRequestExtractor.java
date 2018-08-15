package com.earnest.crawler.core.extractor;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;


import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractHttpRequestExtractor implements HttpRequestExtractor {


    @Override
    public Set<HttpUriRequest> extract(HttpResponseResult<String> responseResult) {
        HttpUriRequest httpRequest = responseResult.getHttpRequest();

        RequestBuilder requestBuilder = RequestBuilder.copy(httpRequest);
        Set<String> newUris = extractUrl(responseResult);
        log.trace("Get {} new uris by {}", newUris.size(), httpRequest.getURI());
        return newUris.stream().map(url ->
                requestBuilder.setUri(url).build()
        ).collect(Collectors.toSet());
    }


    protected abstract Set<String> extractUrl(HttpResponseResult<String> responseResult);
}
