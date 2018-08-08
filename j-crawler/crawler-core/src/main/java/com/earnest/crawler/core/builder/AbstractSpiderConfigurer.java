package com.earnest.crawler.core.builder;

import org.springframework.util.Assert;

public abstract class AbstractSpiderConfigurer<O> implements Comparable<AbstractSpiderConfigurer<O>> {

    private SpiderBuilder builder;


    AbstractSpiderConfigurer(SpiderBuilder builder) {
        this.builder = builder;
    }


    public SpiderBuilder and() {
        Assert.state(builder != null, "spider builder is null");
        return builder;
    }

    abstract O build();

    abstract int order();

    @Override
    public int compareTo(AbstractSpiderConfigurer<O> o) {
        return Integer.compare(order(), o.order());
    }


}
