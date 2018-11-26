package com.earnest.crawler.proxy;


@FunctionalInterface
public interface HttpProxyPoolAware  {
    void setHttpProxySupplier(HttpProxySupplier httpProxySupplier);
}
