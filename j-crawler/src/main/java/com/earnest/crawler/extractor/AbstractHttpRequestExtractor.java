package com.earnest.crawler.extractor;

import com.earnest.crawler.Browser;
import com.earnest.crawler.HttpResponseResult;
import lombok.extern.slf4j.Slf4j;
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
        if (newUris.size() != 0)
            log.trace("Get {} new uris by {}", newUris.size(), httpRequest.getURI());
        return newUris.stream().map(url ->
                requestBuilder.setUri(url)
                        .setHeader(Browser.REFERER, httpRequest.getRequestLine().getUri())
                        .build()
        ).collect(Collectors.toSet());
    }


    protected abstract Set<String> extractUrl(HttpResponseResult<String> responseResult);
}
