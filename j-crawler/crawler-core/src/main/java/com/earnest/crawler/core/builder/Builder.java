package com.earnest.crawler.core.builder;

@FunctionalInterface
public interface Builder<O> {

    O build();
}
