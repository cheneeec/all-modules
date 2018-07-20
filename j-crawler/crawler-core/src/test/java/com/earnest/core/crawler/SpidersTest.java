package com.earnest.core.crawler;

import com.earnest.core.IQiYi;
import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.crawler.Spiders;
import com.earnest.crawler.core.request.HttpGetRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.stream.Collectors;


public class SpidersTest {

    private static final String jsonFileLocation = "crawler/iqiyi.json";

    public static Spider createCustom() {
        HttpGetRequest iqiyiHttpGetRequest = new HttpGetRequest("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html");
        iqiyiHttpGetRequest.setCookies(new HashMap<String, String>() {{
            put("__uuid", "48c04310-fe61-4b7e-d7ba-2178d31b2ea5");
        }});

        HttpGetRequest httpGetRequest1 = new HttpGetRequest("https://github.com/angular/angular-cli");

        return Spiders.createCustom()
                .from(iqiyiHttpGetRequest)
                .match("/www/4/38-------------4-\\d+-1-iqiyi--.html")
                .thread(5)
                .stopWhen(httpResponse -> {
                    Element body = Jsoup.parse(httpResponse.getContent()).body();
                    return body.select("body > div.mod-page > span.noPage").attr("class").equals("noPage");
                })
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
                .build();
    }

    public static void createJsonConfigurable() {
        Spiders.createJsonConfigurable(jsonFileLocation).start();

    }





}