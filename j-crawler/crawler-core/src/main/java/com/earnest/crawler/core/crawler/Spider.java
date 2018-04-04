package com.earnest.crawler.core.crawler;

import java.util.List;

public interface Spider {
    void start();

    void shutdown();

    List<Runnable> stopNow();


}
