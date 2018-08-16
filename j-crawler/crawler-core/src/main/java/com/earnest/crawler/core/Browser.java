package com.earnest.crawler.core;

public enum Browser {

    IPHONE_X("Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1"),
    GOOGLE("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");


    private final String userAgent;

    public static final String USER_AGENT = "User-Agent";
    public static final String REFERER = "Referer";
    public static final String CONTENT_TYPE = "Content-Type";

    Browser(String userAgent) {
        this.userAgent = userAgent;
    }

    public String userAgent() {
        return this.userAgent;
    }

}
