package com.earnest.crawler.core.response;


import com.earnest.crawler.core.request.HttpRequest;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PageResponse {

    private String content;

    private String contentType;

    private HttpRequest httpRequest;

    private int status;

    public PageResponse(String content) {
        this.content = content;
    }

    public PageResponse(String content, HttpRequest httpRequest) {
        this(content);
        this.httpRequest = httpRequest;
    }

}
