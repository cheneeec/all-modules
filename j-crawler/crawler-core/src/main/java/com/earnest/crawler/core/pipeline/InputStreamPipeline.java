package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.InputStreamHttpResponseResult;

import java.io.InputStream;

@FunctionalInterface
public interface InputStreamPipeline<R> extends Pipeline<InputStream, InputStreamHttpResponseResult, R> {
}
