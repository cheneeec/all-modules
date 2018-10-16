package com.earnest.crawler.downloader;

import com.earnest.crawler.StringResponseResult;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.Closeable;

public interface Downloader extends Closeable {

    StringResponseResult download(HttpUriRequest request);
}
