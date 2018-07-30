package com.earnest.video;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.Browser;
import com.earnest.crawler.core.response.PageResponse;
import com.earnest.video.entity.IQiYi;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClientExample {
    @Test
    public void example() throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpUriRequest get = RequestBuilder.get("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
                .setHeader(Browser.USER_AGENT, Browser.GOOGLE.userAgent())
                .build();


        List<IQiYi> execute = httpClient.execute(get, new AbstractResponseHandler<List<IQiYi>>() {
            @Override
            public List<IQiYi> handleEntity(HttpEntity entity) throws IOException {
                Document document = Jsoup.parse(EntityUtils.toString(entity));
                Elements elements = document.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");
                return elements.stream().map(e -> {
                    IQiYi iQiYi = new IQiYi();
                    Elements a = e.select("div.site-piclist_pic > a");
                    iQiYi.setPlayValue(a.attr("href"));
                    iQiYi.setImage("http:" + a.select("img").attr("src"));
                    iQiYi.setTitle(a.select("img").attr("title"));
                    iQiYi.setPlayInfo(a.select("span.icon-vInfo").text());
                    iQiYi.setCategory("动漫");
                    return iQiYi;
                }).collect(Collectors.toList());
            }

            @Override
            public List<IQiYi> handleResponse(HttpResponse response) throws HttpResponseException, IOException {
                System.out.println("===========");
                System.out.println(JSONObject.toJSONString(response, SerializerFeature.PrettyFormat));
                System.out.println("===========");

                return super.handleResponse(response);
            }
        });

        System.out.println(JSONObject.toJSONString(execute, SerializerFeature.PrettyFormat));
    }

    @Test
    public void headers() {

    }

}