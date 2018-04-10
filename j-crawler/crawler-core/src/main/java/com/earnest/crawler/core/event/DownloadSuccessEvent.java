package com.earnest.crawler.core.event;

import com.earnest.crawler.core.response.HttpResponse;

public class DownloadSuccessEvent extends BasicEvent {

    public DownloadSuccessEvent(HttpResponse httpResponse) {
        super(httpResponse);
    }

    @Override
    public HttpResponse getSource() {
        return (HttpResponse) source;
    }



}
