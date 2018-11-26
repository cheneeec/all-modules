package com.earnest.crawler.proxy;

import java.io.Closeable;
import java.util.Optional;
import java.util.function.Supplier;

public interface HttpProxySupplier extends Supplier<Optional<HttpProxy>>, Closeable {
    /**
     * @return 是否有可用的代理连接。
     */
    boolean hasAvailableProxy();

    void delete(HttpProxy httpProxy);

}
