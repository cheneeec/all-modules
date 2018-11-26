package com.earnest.video.core.exception;

import java.io.IOException;

public class ValueParseException extends IOException {
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
