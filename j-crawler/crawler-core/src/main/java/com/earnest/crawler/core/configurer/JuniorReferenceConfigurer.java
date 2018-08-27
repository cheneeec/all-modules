package com.earnest.crawler.core.configurer;

public interface JuniorReferenceConfigurer<T extends Configurer> extends Configurer{

    T next();
}
