package com.earnest.crawler.core.response;


import com.earnest.crawler.core.request.HttpRequest;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
public class HttpResponse {


    private String content;

    private String contentType;

    private HttpRequest httpRequest;

    private int status;

    public HttpResponse(String content) {
        this.content = content;
    }

}
