package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.HttpResponseResult;


@FunctionalInterface
public interface Pipeline<TYPE, T extends HttpResponseResult<TYPE>, RETURN> {

    RETURN pipe(T t);
}
