package com.earnest.video.spider;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class IQiYiAnimationEpisodeSpider {

    @Test(timeout = Long.MAX_VALUE)
    public void spider() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGetRequest = new HttpGet("https://www.youtube.com/");
        CloseableHttpResponse execute = httpClient.execute(httpGetRequest);
        System.out.println(EntityUtils.toString(execute.getEntity()));

    }

    @Test
    public void http() throws IOException {




    }



}
