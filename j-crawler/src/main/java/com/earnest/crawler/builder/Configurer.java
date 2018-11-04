package com.earnest.crawler.builder;


abstract class Configurer {
    abstract void init();


    abstract void configure();

    protected int order() {
        return Integer.MAX_VALUE;
    }

}
