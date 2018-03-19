package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.request.HttpRequest;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public interface Scheduler extends BlockingQueue<HttpRequest> {
}
