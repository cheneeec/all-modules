package com.earnest.video.exception;


public class ValueParseException extends Exception {
    public ValueParseException() {
        super();
    }

    public ValueParseException(String message) {
        super(message);
    }

    public ValueParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
