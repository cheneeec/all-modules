package com.earnest.crawler.core.proxy;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class SimpleHttpProxyProviderTest {
    private SimpleHttpProxyProvider httpProxyProvider = SimpleHttpProxyProvider.INSTANCE;

    @Test
    public void provideHttpProxies() throws InterruptedException {
        httpProxyProvider.initializeHttpProxyPool();
        Optional<HttpProxy> httpProxy = httpProxyProvider.get();


        assertTrue(httpProxy.isPresent());
    }
}