package com.earnest.video;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.earnest.crawler.core.builder.SpiderBuilder;
import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.request.Browser;
import com.earnest.video.entity.IQiYi;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication(scanBasePackages = "com.earnest")
public class VideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
        System.out.println("=====================================");
        Spider spider = new SpiderBuilder()
                .request().method(Connection.Method.GET)
                .from("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
                .and()
                .global()
                .setThreadNumber(10)
                .userAgent(Browser.GOOGLE.userAgent())
                .and()
                .pipeline()
                .asFile("D:/")
               /* .cssSelector(response -> {
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

//                    System.out.println(JSONObject.toJSONString(iQiYis, SerializerFeature.PrettyFormat));
                })*/.and()
                .extract().match("/www/4/38-------------4-\\d+-1-iqiyi--.html")
                .and()
                .scheduler().blockingUnique(10000)
                .and()
                .build();

        spider.start();

    }


}
