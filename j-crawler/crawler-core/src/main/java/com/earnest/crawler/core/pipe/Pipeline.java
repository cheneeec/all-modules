package com.earnest.crawler.core.pipe;

import com.earnest.crawler.core.response.HttpResponse;

import java.util.Collection;

@FunctionalInterface
public interface Pipeline<T> {
    Collection<T> pipe(HttpResponse httpResponse);
}
