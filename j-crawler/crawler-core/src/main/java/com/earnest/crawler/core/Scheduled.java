package com.earnest.crawler.core;

/**
 * 可调度的。
 */
@FunctionalInterface
public interface Scheduled {

    void cron(String cron);
}
