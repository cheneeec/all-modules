package com.earnest.crawler.core;

public class RawTextHttpResponseResult extends BasicHttpResponseResult<String> {

    private final String content;

    public RawTextHttpResponseResult(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }

}
