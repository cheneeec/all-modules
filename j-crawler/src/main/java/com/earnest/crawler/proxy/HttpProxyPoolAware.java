package com.earnest.crawler.proxy;


@FunctionalInterface
public interface HttpProxyPoolAware  {
    void setHttpProxyPool(HttpProxyPool httpProxyPool);
}
