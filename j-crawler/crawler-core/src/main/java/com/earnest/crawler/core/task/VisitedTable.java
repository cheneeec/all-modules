package com.earnest.crawler.core.task;

import java.util.Collection;

public interface VisitedTable {
    void add(Task task);

    void destory();

    Collection<? extends Task> getAll();

    boolean isVisited(Task task);

    boolean remove();
}
