package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.request.HttpRequest;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

public interface Downloader<T extends Serializable,R extends HttpResponseResult<T>> extends Closeable {
    R download(HttpRequest request) throws IOException;
}
