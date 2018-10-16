package com.earnest.crawler.builder;

@FunctionalInterface
public interface Builder<O> {

    O build();
}
