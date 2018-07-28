package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.request.HttpRequest;

import java.io.Closeable;
import java.io.IOException;

public interface Downloader< T> extends Closeable {
    T download(HttpRequest request) throws IOException;
}
