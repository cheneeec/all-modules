package com.earnest.crawler.core.event;

import com.earnest.crawler.core.request.HttpRequest;
import lombok.Getter;

public class DownloadErrorEvent extends BasicEvent {
    @Getter
    private final String error;

    public DownloadErrorEvent(HttpRequest httpRequest, Exception error) {
        super(httpRequest);
        this.error = error.getMessage();
    }

    @Override
    public HttpRequest getSource() {
        return (HttpRequest) super.getSource();
    }
}
