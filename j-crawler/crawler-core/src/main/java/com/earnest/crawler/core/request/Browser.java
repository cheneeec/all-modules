package com.earnest.crawler.core.request;

public enum Browser {


    GOOGLE("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");

    private final String userAgent;

    public static final String USER_AGENT = "User-Agent";
    public static final String REFERER = "Referer";
    public static final String CONTENT_TYPE = "content-type";

    Browser(String userAgent) {
        this.userAgent = userAgent;
    }

    public String userAgent() {
        return this.userAgent;
    }

}
