package com.earnest.video.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
public class UnsupportedPlatformException extends RuntimeException {
    public UnsupportedPlatformException(String message) {
        super(message);
    }
}
