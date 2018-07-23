package com.earnest.crawler.core.event;

import com.earnest.crawler.core.response.PageResponse;

public class DownloadSuccessEvent extends BasicEvent {

    public DownloadSuccessEvent(PageResponse pageResponse) {
        super(pageResponse);
    }

    @Override
    public PageResponse getSource() {
        return (PageResponse) source;
    }



}
