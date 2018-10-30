package com.earnest.crawler.proxy;

import java.util.Optional;
import java.util.function.Supplier;

public interface HttpProxyPool extends Supplier<Optional<HttpProxy>> {

    int getSize();

    void initializeHttpProxyPool() throws Exception;


}
