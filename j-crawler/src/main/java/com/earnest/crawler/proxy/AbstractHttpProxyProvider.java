package com.earnest.crawler.proxy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 提供一个代理连接的供给。
 */
@Slf4j
public abstract class AbstractHttpProxyProvider implements HttpProxyPool {

    private final ConcurrentHashMap<Integer, HttpProxy> httpProxiesMap = new ConcurrentHashMap<>();

    protected final AtomicInteger size = new AtomicInteger();

    private final AtomicBoolean invokedInitializeHttpProxyPoolMethod = new AtomicBoolean();


    /**
     * @return 随机获取一个{@link HttpProxy}。
     */
    @Override
    public Optional<HttpProxy> get() {
        if (httpProxiesMap.isEmpty()) return Optional.empty();
        return Optional.ofNullable(httpProxiesMap.get(RandomUtils.nextInt(0, size.get())));
    }


    protected int putHttpProxies(List<HttpProxy> httpProxies) {
        int start = size.get();
        httpProxies
                .forEach(httpProxy -> httpProxiesMap.put(size.getAndIncrement(), httpProxy));
        int end = size.get();
        return end - start;
    }

    protected void putHttpProxy(HttpProxy httpProxy) {
        httpProxiesMap.put(size.getAndIncrement(), httpProxy);
    }


    @Override
    public int getSize() {
        return httpProxiesMap.size();
    }

    @Override
    public void initializeHttpProxyPool()  throws  Exception{
        if (!invokedInitializeHttpProxyPoolMethod.get()) {
            doInitializeHttpProxyPool();
            invokedInitializeHttpProxyPoolMethod.set(true);
        }
    }

    protected abstract void doInitializeHttpProxyPool() throws  Exception;

}
