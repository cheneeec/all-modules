package com.earnest.crawler.core.exception;

public class TakeTimeoutException extends Exception {

    public TakeTimeoutException(String msg) {
        super(msg);
    }

    public TakeTimeoutException() {
    }
}
