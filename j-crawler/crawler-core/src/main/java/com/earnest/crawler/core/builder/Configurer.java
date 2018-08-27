package com.earnest.crawler.core.builder;


abstract class Configurer {


    abstract void init();


    abstract void configure();

    protected int order() {
        return Integer.MAX_VALUE;
    }

}
