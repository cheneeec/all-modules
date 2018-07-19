package com.earnest.crawler.core.request;

public enum BrowserUserAgent {


    GOOGLE("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36");

    private String userAgent;
    public static String HEADER = "User-Agent";

    BrowserUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String value() {
        return this.userAgent;
    }

}
