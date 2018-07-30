package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.request.HttpRequest;
import org.apache.http.HttpResponse;

import java.io.Closeable;
import java.io.IOException;

public interface Downloader extends Closeable {
    HttpResponse download(HttpRequest request) throws IOException;
}
