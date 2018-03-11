package com.earnest.crawler.core.pipe;

@FunctionalInterface
public interface Pipeline<R, T> {

    T pipe(R r);
}
