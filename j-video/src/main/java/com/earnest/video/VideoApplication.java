package com.earnest.video;


import com.earnest.crawler.core.builder.SpiderBuilder;
import com.earnest.crawler.core.spider.Spider;
import com.earnest.crawler.core.Browser;
import org.jsoup.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.earnest")
public class VideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
        System.out.println("=====================================");
        Spider spider = new SpiderBuilder()
                .request().method(Connection.Method.GET).from("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
                .and()
                .global().setThreadNumber(10).userAgent(Browser.GOOGLE.userAgent())
                .and()
                .pipeline().asFile("D:/")
                .and()
                .extract().range("http://list.iqiyi.com/www/4/38-------------4-${1~30}-1-iqiyi--.html")
                .and()
                .scheduler().fixed()
                .and()
                .build();

        spider.start();


    }


}
