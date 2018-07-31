package com.earnest.crawler.core;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamHttpResponseResult extends BasicHttpResponseResult<InputStream> implements Closeable {

    private final InputStream content;

    public InputStreamHttpResponseResult(InputStream content) {
        this.content = content;
    }

    @Override
    public InputStream getContent() {
        return content;
    }

    @Override
    public void close() throws IOException {
        if (content != null) {
            content.close();
        }

    }
}
