package com.earnest.crawler.core.downloader;


import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;

import java.io.Closeable;

public interface Downloader extends Closeable {


    HttpResponse download(HttpRequest request);


}
