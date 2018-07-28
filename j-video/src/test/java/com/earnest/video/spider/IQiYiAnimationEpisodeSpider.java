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
        HttpGet httpGetRequest = new HttpGet("http://cache.video.iqiyi.com/jp/avlist/202861101/7/50/?albumId=202861101&pageNum=50&pageNo=7&callback=window.Q.__callbacks__.cb9iivgm");
        CloseableHttpResponse execute = httpClient.execute(httpGetRequest);
        System.out.println(EntityUtils.toString(execute.getEntity()));

    }

    @Test
    public void http() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse execute = httpClient.execute(new HttpGet("http://www.iqiyi.com/a_19rrhb3xvl.html#vfrm=2-4-0-1"));

        System.out.println(EntityUtils.toString(execute.getEntity()));



    }



}
