package com.earnest.crawler.core.proxy;


@FunctionalInterface
public interface HttpProxyPoolAware  {
    void setHttpProxyPool(HttpProxyPool httpProxyPool);
}
