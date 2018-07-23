package com.earnest.crawler.core.pipe;

import com.earnest.crawler.core.response.PageResponse;

@FunctionalInterface
public interface Pipeline<T> {
     T pipe(PageResponse pageResponse);
}
