package com.earnest.crawler.core.task;

import java.util.Iterator;


/**
 * 存放所有未爬取的{@link Task}
 */
public interface TaskQueue extends Iterator<Task> {

    boolean add(Task task);

    int getSize();

    Task getTask(String task);

    Task peek();

    Task poll();

}
