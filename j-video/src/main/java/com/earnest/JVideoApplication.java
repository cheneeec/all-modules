package com.earnest;

import com.earnest.crawler.core.crawler.Spiders;
import com.earnest.crawler.core.request.HttpGetRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.stream.Collectors;

@SpringBootApplication
public class JVideoApplication {

    private static final String jsonFileLocation = "crawler/iqiyi.json";

    public static void createCustom() {
        HttpGetRequest httpGetRequest = new HttpGetRequest("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html");
        httpGetRequest.setCookies(new HashMap<String, String>() {{
            put("__uuid", "48c04310-fe61-4b7e-d7ba-2178d31b2ea5");
        }});
        Spiders.createCustom()
                .from(httpGetRequest)
                .match("/www/4/38-------------4-\\d+-1-iqiyi--.html")
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
                })
                .addConsumer((h) -> {
                })
                .build()
                .start();

    }


    public static void main(String[] args) {
        SpringApplication.run(JVideoApplication.class, args);
        createCustom();
        System.out.println("当前活动的线程：" + Thread.activeCount());

    }


}
