package com.earnest.crawler.core.worker;

import com.earnest.crawler.IQiYi;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.response.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SpiderTest {


    public static void main(String[] args) throws InterruptedException {
        ISpider spider = new Spider();
        ISpider iSpider = spider.from("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
                .match("/www/4/38-------------4-\\d-1-iqiyi--.html")
                .thread(5)
                .pipeline(httpResponse -> {
                    Element body = Jsoup.parse(httpResponse.getContent()).body();
                    Elements elements = body.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");
                    return elements.stream().map(e -> {
                        Elements img = e.select("div.site-piclist_pic > a > img");
                        String src = img.attr("src");
                        String title = img.attr("title");
                        String href = e.select("div.site-piclist_pic > a").attr("href");
                        return new IQiYi(title, href, src);
                    }).collect(Collectors.toList());
                }).start();

        TimeUnit.SECONDS.sleep(5);
        iSpider.pause();
        System.out.println(iSpider.isPause());
        TimeUnit.SECONDS.sleep(60);
        iSpider.restart();
        System.out.println(iSpider.isPause());

    }
}