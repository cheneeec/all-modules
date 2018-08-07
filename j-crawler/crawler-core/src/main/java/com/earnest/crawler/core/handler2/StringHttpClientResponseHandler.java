package com.earnest.crawler.core.handler2;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class StringHttpClientResponseHandler extends AbstractHttpClientResponseHandler<String> {
    @Override
    protected String getContent(HttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }
}
