package com.earnest.video;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.builder.SpiderBuilder;
import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.handler2.AbstractHttpClientResponseHandler;
import com.earnest.crawler.core.request.Browser;
import com.earnest.video.entity.IQiYi;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClientExample {
    CloseableHttpClient httpClient = HttpClients.createDefault();

    HttpUriRequest get = RequestBuilder.get("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
            .setHeader(Browser.USER_AGENT, Browser.GOOGLE.userAgent())
            .build();

    HttpClientContext httpClientContext = new HttpClientContext();

    @Test
    public void example() throws IOException {

        List<IQiYi> execute = httpClient.execute(get, new AbstractResponseHandler<List<IQiYi>>() {
            @Override
            public List<IQiYi> handleEntity(HttpEntity entity) throws IOException {

                FileCopyUtils.copy(entity.getContent(), new FileOutputStream(new File("D:/1.txt")));
                String string = EntityUtils.toString(entity);


                Document document = Jsoup.parse(string);

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

        });
    }

    @Test
    public void httpClientResponseHandler() throws IOException {

        HttpClientContext httpClientContext = new HttpClientContext();

        HttpResponseResult<String> execute = httpClient.execute(get, new AbstractHttpClientResponseHandler<String>() {
            @Override
            protected String getContent(HttpResponse response) throws IOException {
                return EntityUtils.toString(response.getEntity());
            }
        }, httpClientContext);


        System.out.println(JSONObject.toJSONString(execute, SerializerFeature.PrettyFormat));
        System.out.println("============");
        httpClientContext.getCookieStore().getCookies().forEach(c -> System.out.println(c.getName() + ":" + c.getValue()));
    }

    @Test
    public void fileSpider() {


    }

    public static void main(String[] args) {
        Spider spider = new SpiderBuilder()
                .request().method(Connection.Method.GET)
                .from("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
                .and()
                .global()
                .setThread(10).userAgent(Browser.GOOGLE.userAgent())
                .and()
                .pipeline()
                .cssSelector(response -> {
                    Document document = response.getContent();
                    Elements elements = document.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");

                    List<IQiYi> iQiYis = elements.stream().map(e -> {
                        IQiYi iQiYi = new IQiYi();
                        Elements a = e.select("div.site-piclist_pic > a");
                        iQiYi.setPlayValue(a.attr("href"));
                        iQiYi.setImage("http:" + a.select("img").attr("src"));
                        iQiYi.setTitle(a.select("img").attr("title"));
                        iQiYi.setPlayInfo(a.select("span.icon-vInfo").text());
                        iQiYi.setCategory("动漫");
                        return iQiYi;
                    }).collect(Collectors.toList());

                    System.out.println(JSONObject.toJSONString(iQiYis, SerializerFeature.PrettyFormat));
                }).and()
                .extract().match("/www/4/38-------------4-\\d+-1-iqiyi--.html")
                .and()
                .build();

        spider.start();


    }


}