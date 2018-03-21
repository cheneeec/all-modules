package com.earnest.crawler.core.handler;

import org.junit.Test;

public class HttpResponseHandlerTest {

    @Test
    public void handle() {


        HttpResponseHandler responseHandler=new RegexHttpResponseHandler("(\\w+\\.){2}\\w+");
    }
}