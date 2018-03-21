package com.earnest.crawler.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class JsoupExample {

    @Test
    public void documentExample() throws Exception {
        Document document = Jsoup.connect("http://www.xicidaili.com/nn/1")
                .ignoreContentType(true)
                .header("_1_auth", "cxg3CQeblGyCq0260wRfF2qYRzw9Xs")
                .header("_1_ver", "0.3.0")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .referrer("https://www.baidu.com/link?url=W1ze2OqPIykQ8eD140mhiQht0qHq9rKYaI_-J_yYDhBMQlhSGlMqZ6F_LfqrOPD_&ck=7251.1.91.404.144.387.141.131&shh=www.baidu.com&sht=baiduhome_pg&wd=&eqid=d3e5b93e0000adc5000000055a9e03fe")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                .header("Upgrade-Insecure-Requests", "1")
                .get();
        System.out.println(document);

        System.out.println(document.body().select("#body > div.pagination > a:nth-child(3)").attr("abs:href"));

        Assert.assertNotNull(document);

    }
}
