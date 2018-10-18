package com.earnest.crawler;

import java.io.Closeable;

public interface Spider extends Closeable {


    void start();

    default void stop() {
        throw new UnsupportedOperationException(this.getClass()+" can't stop");
    }


}
