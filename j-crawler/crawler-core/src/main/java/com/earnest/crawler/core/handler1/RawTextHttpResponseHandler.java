package com.earnest.crawler.core.handler1;

import com.earnest.crawler.core.RawTextHttpResponseResult;

@FunctionalInterface
public interface RawTextHttpResponseHandler<T> extends HttpResponseHandler<String, RawTextHttpResponseResult, T> {


}
