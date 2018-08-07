package com.earnest.crawler.core.handler1;

import com.earnest.crawler.core.HttpResponseResult;



@FunctionalInterface
public interface HttpResponseHandler<T,R> {

    R handle(HttpResponseResult<T> responseResult);

}
