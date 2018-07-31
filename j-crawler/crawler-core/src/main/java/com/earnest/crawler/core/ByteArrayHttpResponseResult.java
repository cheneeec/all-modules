package com.earnest.crawler.core;

public class ByteArrayHttpResponseResult extends BasicHttpResponseResult<Byte[]> {

    private final Byte[] content;

    public ByteArrayHttpResponseResult(Byte[] content) {
        this.content = content;
    }

    @Override
    public Byte[] getContent() {
        return content;
    }
}
