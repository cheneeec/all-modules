package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.extractor.RegexHttpRequestExtractor;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class HttpUriRequestExtractorConfigurer<T, R extends HttpResponseResult<T>> extends AbstractSpiderConfigurer<Map<String, HttpRequestExtractor<T, R>>> {

    private final Map<String, HttpRequestExtractor<T, R>> requestExtractors = new LinkedHashMap<>(5);

    private HttpRequestExtractor<T, R> defaultRegexHttpRequestExtractor;

    public HttpUriRequestExtractorConfigurer(SpiderBuilder builder) {
        super(builder);
    }

    public HttpUriRequestExtractorConfigurer<T, R> defaultMatch(String pattern) {
        this.defaultRegexHttpRequestExtractor = (HttpRequestExtractor<T, R>) new RegexHttpRequestExtractor(pattern);
        return this;
    }

    public HttpUriRequestExtractorConfigurer<T, R> match(String domainPath, String pattern) {
        requestExtractors.put(domainPath, (HttpRequestExtractor<T, R>) (new RegexHttpRequestExtractor(pattern)));
        return this;
    }


    @Override
    Map<String, HttpRequestExtractor<T, R>> build() {
        if (defaultRegexHttpRequestExtractor != null) {
            requestExtractors.put("**", defaultRegexHttpRequestExtractor);
        }
        return requestExtractors;
    }


}
