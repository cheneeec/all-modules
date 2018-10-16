package com.earnest.crawler.builder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;


import java.util.List;
import java.util.Map;


@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class SharedSpiderConfigurer extends Configurer {

    @Setter(AccessLevel.PACKAGE)
    private SpiderBuilder builder;

    @Setter(value = AccessLevel.PACKAGE)
    protected Map<Class<?>, List<?>> sharedObjectMap;


    public SpiderBuilder and() {
        Assert.state(builder != null, "spider builder is null");

        return builder;
    }


    @Override
    void init() {

    }

    @Override
    void configure() {

    }
}
