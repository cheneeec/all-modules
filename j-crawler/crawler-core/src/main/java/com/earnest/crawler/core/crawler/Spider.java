package com.earnest.crawler.core.crawler;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface Spider extends Closeable {
    void start();

    void shutdown();

    List<Runnable> shutdownNow();

    boolean isRunning();

    String getName();

    void setName(String name);
}
