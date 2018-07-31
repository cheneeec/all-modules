package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.ByteArrayHttpResponseResult;

@FunctionalInterface
public interface ByteArrayPipeline<R> extends Pipeline<Byte[],ByteArrayHttpResponseResult, R> {

}
