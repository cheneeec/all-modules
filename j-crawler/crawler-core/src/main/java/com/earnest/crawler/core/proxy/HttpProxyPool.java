package com.earnest.crawler.core.proxy;

import java.util.Optional;
import java.util.function.Supplier;

public interface HttpProxyPool extends Supplier<Optional<HttpProxy>> {

    int getSize();

    void initializeHttpProxyPool();

    /**
     * @return 是否执行过 {@link #initializeHttpProxyPool()}。
     */
    boolean Initialized();

}
