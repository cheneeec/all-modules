package com.earnest.video.spider;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.crawler.Spiders;
import com.earnest.video.entity.IQiYi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class IQiYiSpider implements Spider {

    private final Spider indexSpider;

    public IQiYiSpider() {
        indexSpider = createIQiYiSpider();
    }

    private Spider createIQiYiSpider() {
        return Spiders.createCustom()
                .from("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html")
                .match("/www/4/38-------------4-\\d+-1-iqiyi--.html")
                .pipeline(httpResponse -> {
                    String fromUrl = httpResponse.getHttpRequest().getUrl();
                    Date nowDate = Calendar.getInstance().getTime();
                    Element element = Jsoup.parse(httpResponse.getContent()).body();
                    Elements elements = element.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");
                    return elements.stream().map(e -> {
                        IQiYi iQiYi = new IQiYi();
                        iQiYi.setFromUrl(fromUrl);
                        iQiYi.setCollectTime(nowDate);
                        iQiYi.setPlayValue("http:" + e.select("div.site-piclist_pic > a").attr("href"));
                        iQiYi.setImage("http:" + e.select("div.site-piclist_pic > a > img").attr("src"));
                        iQiYi.setTitle("http:" + e.select("div.site-piclist_pic > a > img").attr("title"));
                        iQiYi.setOrigin("爱奇艺");
                        iQiYi.setCategory("动漫");
                        return iQiYi;
                    }).collect(Collectors.toList());
                })
                .thread(5)
                .build();
    }


    @Override
    public void start() {
        indexSpider.start();
    }

    @Override
    public void shutdown() {
        indexSpider.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return indexSpider.shutdownNow();
    }

    @Override
    public boolean isRunning() {
        return indexSpider.isRunning();
    }

    @Override
    public void close() throws IOException {
        indexSpider.close();
    }
}
