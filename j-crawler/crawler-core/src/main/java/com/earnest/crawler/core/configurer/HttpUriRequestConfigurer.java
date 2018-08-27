package com.earnest.crawler.core.configurer;


public class HttpUriRequestConfigurer implements SuperiorReferenceConfigurer<SharedReferenceConfigurer>,JuniorReferenceConfigurer<HttpUriRequestPropertyConfigurer>{


    @Override
    public HttpUriRequestPropertyConfigurer next() {
        return null;
    }

    @Override
    public SharedReferenceConfigurer and() {
        return null;
    }
}
