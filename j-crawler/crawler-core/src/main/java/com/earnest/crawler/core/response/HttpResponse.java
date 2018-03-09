package com.earnest.crawler.core.response;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpRequest;



import java.util.Objects;

@Data
@NoArgsConstructor
public class HttpResponse {


    private String content;

    private String contentType;

    private String charset;

    private HttpRequest httpRequest;

    private int status;

    public HttpResponse(String content) {
        this.content = content;
    }

}
