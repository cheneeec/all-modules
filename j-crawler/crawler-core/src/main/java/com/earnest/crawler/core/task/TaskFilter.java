package com.earnest.crawler.core.task;

public interface TaskFilter {
    boolean isFiltered(Task task);
}
