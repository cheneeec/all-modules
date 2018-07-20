package com.earnest.crawler.core.builder;

import org.springframework.util.Assert;

public abstract class AbstractSpiderConfigurer<O> implements Configurer {

    private SpiderBuilder builder;

    private O configured;

    @Override
    public void init() throws Exception {

    }

    @Override
    public void configure() throws Exception {

    }

    public SpiderBuilder and() {
        Assert.state(builder != null, "spider builder is null");
        return builder;
    }


}
