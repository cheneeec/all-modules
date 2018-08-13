package com.earnest.crawler.core.exception;

public class TakeTimeoutException extends RuntimeException {

    public TakeTimeoutException(String msg) {
        super(msg);
    }

    public TakeTimeoutException() {
    }
}
