package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.StringResponseResult;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.Closeable;

public interface Downloader extends Closeable {

    StringResponseResult download(HttpUriRequest request);
}
