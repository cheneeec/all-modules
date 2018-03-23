package com.earnest.crawler.core.worker;

import com.earnest.crawler.IQiYi;
import com.earnest.crawler.core.crawler.BasicSpider;
import com.earnest.crawler.core.crawler.Spider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.stream.Collectors;

public class SpiderTest {


    public static void main(String[] args) throws InterruptedException {
        Spider spider = new BasicSpider();

        Spider iSpider = spider.from("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
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



    }
}