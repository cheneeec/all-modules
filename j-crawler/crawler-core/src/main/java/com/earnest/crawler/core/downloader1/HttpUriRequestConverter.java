package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.request.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;

public abstract class HttpUriRequestConverter {


    public static HttpUriRequest convert(HttpRequest request) {

        Assert.notNull(request, "HttpRequest is null");
        Assert.hasText(request.getUrl(), "url is empty or null");

        //build a basic request
        RequestBuilder requestBuilder = RequestBuilder.create(request.getMethod())
                .setCharset(Charset.forName(request.getCharset()))
                .setUri(request.getUrl());

        //add headers
        if (!CollectionUtils.isEmpty(request.getHeaders()))
            request.getHeaders().forEach(requestBuilder::addHeader);

        //add parameters
        if (!CollectionUtils.isEmpty(request.getParameters()))
            request.getParameters().forEach(requestBuilder::addParameter);

        return requestBuilder.build();
    }

}
