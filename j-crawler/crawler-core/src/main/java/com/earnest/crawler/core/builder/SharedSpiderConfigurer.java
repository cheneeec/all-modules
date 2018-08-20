package com.earnest.crawler.core.builder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;


import java.util.List;
import java.util.Map;


@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class SharedSpiderConfigurer<O> extends Configurer implements Comparable<SharedSpiderConfigurer<O>> {

    @Setter(AccessLevel.PACKAGE)
    private SpiderBuilder builder;


    @Setter(value = AccessLevel.PACKAGE)
    protected Map<Class<?>, List<?>> sharedObjectMap;


    public SpiderBuilder and() {
        Assert.state(builder != null, "spider builder is null");

        return builder;
    }


    protected int order() {
        return Integer.MAX_VALUE;
    }


    @Override
    public int compareTo(SharedSpiderConfigurer<O> o) {
        return Integer.compare(order(), o.order());
    }


    @Override
    void init() {

    }

    @Override
    void configure() {

    }
}
