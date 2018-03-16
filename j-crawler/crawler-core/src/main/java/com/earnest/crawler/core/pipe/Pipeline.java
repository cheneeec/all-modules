package com.earnest.crawler.core.pipe;

import com.earnest.crawler.core.response.HttpResponse;

@FunctionalInterface
public interface Pipeline<T> {
    T pipe(HttpResponse httpResponse);
}
