package com.earnest.core.downloader;

import org.apache.http.HttpResponse;

public abstract class Pipeline<T> {

    public abstract T pipe(HttpResponse httpResponse);


}
