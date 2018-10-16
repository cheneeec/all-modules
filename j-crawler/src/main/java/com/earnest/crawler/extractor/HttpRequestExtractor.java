package com.earnest.crawler.extractor;


import com.earnest.crawler.HttpResponseResult;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.Set;

@FunctionalInterface
public interface HttpRequestExtractor {

    Set<HttpUriRequest> extract(HttpResponseResult<String> responseResult);

}
