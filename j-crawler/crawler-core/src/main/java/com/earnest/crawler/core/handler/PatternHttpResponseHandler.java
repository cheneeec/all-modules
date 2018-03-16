package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;


import java.util.List;
import java.util.regex.Pattern;
@Getter
@Setter
public class PatternHttpResponseHandler extends AbstractHttpResponseHandler {

    private final Pattern urlPattern;


    public PatternHttpResponseHandler(String urlPattern) {
        Assert.hasText(urlPattern, "this String argument:[urlPattern] is empty or null");
        this.urlPattern = Pattern.compile(urlPattern);
    }

    public PatternHttpResponseHandler(){
        this("^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+");
    }

    @Override
    protected List<HttpRequest> extract(HttpResponse httpResponse) {
        return null;
    }


}
