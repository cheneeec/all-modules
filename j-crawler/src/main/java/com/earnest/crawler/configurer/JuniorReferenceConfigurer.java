package com.earnest.crawler.configurer;

public interface JuniorReferenceConfigurer<T extends Configurer> extends Configurer{

    T next();
}
