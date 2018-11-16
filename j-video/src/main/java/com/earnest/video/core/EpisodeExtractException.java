package com.earnest.video.core;

import java.io.IOException;


public class EpisodeExtractException extends IOException {
    public EpisodeExtractException() {
    }

    public EpisodeExtractException(String message) {
        super(message);
    }

    public EpisodeExtractException(String message, Throwable cause) {
        super(message, cause);
    }
}
