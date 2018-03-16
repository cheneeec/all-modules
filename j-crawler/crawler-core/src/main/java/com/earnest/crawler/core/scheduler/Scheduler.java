package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.request.HttpRequest;

import java.util.Queue;

public interface Scheduler extends Queue<HttpRequest> {
}
