package com.earnest.crawler.core.spider;

import java.io.Closeable;

public interface Spider extends Closeable {


    void start();

    default void stop() {
        throw new UnsupportedOperationException(this.getClass()+" can't stop");
    }

    boolean isRunning();

}
