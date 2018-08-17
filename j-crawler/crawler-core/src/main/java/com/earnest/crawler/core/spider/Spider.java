package com.earnest.crawler.core.spider;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface Spider extends Closeable {
    void start();

    void stop();


    boolean isRunning();

}
