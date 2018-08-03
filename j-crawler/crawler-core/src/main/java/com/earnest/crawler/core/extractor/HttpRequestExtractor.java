package com.earnest.crawler.core.extractor;


import com.earnest.crawler.core.HttpResponseResult;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Set;

@FunctionalInterface
public interface HttpRequestExtractor {

    Set<HttpUriRequest> extract(HttpResponseResult<String> responseResult);

}
