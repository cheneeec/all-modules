package com.earnest.crawler.extractor;

import com.earnest.crawler.HttpResponseResult;

import java.util.Collections;
import java.util.Set;

public class EmptyHttpRequestExtractor extends AbstractHttpRequestExtractor {
    @Override
    protected Set<String> extractUrl(HttpResponseResult<String> responseResult) {
        return Collections.emptySet();
    }
}
