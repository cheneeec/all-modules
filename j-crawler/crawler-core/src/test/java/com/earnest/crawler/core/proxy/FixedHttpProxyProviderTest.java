package com.earnest.crawler.core.proxy;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class FixedHttpProxyProviderTest {
    private FixedHttpProxyProvider httpProxyProvider = new FixedHttpProxyProvider();

    @Test
    public void provideHttpProxies() throws InterruptedException {
        httpProxyProvider.initializeHttpProxyPool();
        Optional<HttpProxy> httpProxy = httpProxyProvider.get();


        assertTrue(httpProxy.isPresent());
    }
}