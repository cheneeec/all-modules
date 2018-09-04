package com.earnest.crawler.core.proxy;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 提供一个代理连接的供给。
 */
public class AbstractHttpProxyProvider implements Supplier<HttpProxy> {

    private final ConcurrentHashMap<Integer, HttpProxy> httpProxiesMap = new ConcurrentHashMap<>();

    private final AtomicInteger size = new AtomicInteger();

    @Override
    public HttpProxy get() {
        if (httpProxiesMap.isEmpty()) return null;
        return httpProxiesMap.get(RandomUtils.nextInt(0, size.get()));
    }

    public int putHttpProxys(List<HttpProxy> httpProxies) {
        int start = size.get();
        httpProxies.stream()
                .distinct()
                .forEach(httpProxy -> httpProxiesMap.put(size.getAndIncrement(), httpProxy));
        int end = size.get();
        return end - start;
    }


}
