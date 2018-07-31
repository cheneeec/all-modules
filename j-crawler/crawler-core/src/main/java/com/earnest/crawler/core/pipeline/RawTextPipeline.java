package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.RawTextHttpResponseResult;

@FunctionalInterface
public interface RawTextPipeline<R> extends Pipeline<String, RawTextHttpResponseResult, R> {

}
