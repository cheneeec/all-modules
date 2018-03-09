package com.earnest.crawler.core.task;

import java.time.LocalDateTime;

/**
 * 描述一个任务的基本信息
 */
public interface Task {

    void abort();

    String getId();

    LocalDateTime getStartTime();

    boolean isAborted();
}
