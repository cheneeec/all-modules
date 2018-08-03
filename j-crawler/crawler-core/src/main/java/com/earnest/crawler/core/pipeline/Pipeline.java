package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.HttpResponseResult;


@FunctionalInterface
public interface Pipeline<T, R> {

    R pipe(HttpResponseResult<T> result);
}
