package com.earnest.crawler.exception;

public class TakeTimeoutException extends RuntimeException {

    public TakeTimeoutException(String msg) {
        super(msg);
    }

    public TakeTimeoutException() {
    }
}
