package com.earnest.crawler.core.event;

import com.earnest.crawler.core.request.HttpRequest;
import lombok.Getter;

public class DownloadErrorEvent extends BasicEvent {
    @Getter
    private final Exception error;

    public DownloadErrorEvent(HttpRequest httpRequest, Exception error) {
        super(httpRequest);
        this.error = error;
    }

    @Override
    public HttpRequest getSource() {
        return (HttpRequest) super.getSource();
    }
}
