package com.earnest.crawler.configurer;


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
