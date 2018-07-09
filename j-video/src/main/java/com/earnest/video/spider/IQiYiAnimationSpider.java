package com.earnest.video.spider;

import com.earnest.crawler.core.crawler.SpiderBuilder;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.IQiYiAnimationCachedVideoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class IQiYiAnimationSpider extends AbstractBaseVideoEntitySpider<IQiYi> implements CommandLineRunner {
    @Autowired
    private IQiYiAnimationCachedVideoService iQiYiAnimationCachedVideoService;

    protected IQiYiAnimationSpider() {
        super("http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html", "/www/4/38-------------4-\\d+-1-iqiyi--.html");
    }

    @Override
    protected Consumer<List<IQiYi>> getSpiderConsumer() {
        return o -> iQiYiAnimationCachedVideoService.save(o);
    }

    @Override
    protected void enhanceSpider(SpiderBuilder spiderBuilder) {
        spiderBuilder.stopWhen(httpResponse -> {
            Element body = Jsoup.parse(httpResponse.getContent()).body();
            return body.select("body > div.mod-page > span.noPage").attr("class").equals("noPage");
        });
    }

    @Override
    protected Pipeline<List<IQiYi>> createPipeline() {
        return httpResponse -> {
            Element element = Jsoup.parse(httpResponse.getContent()).body();
            Elements elements = element.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");
            return elements.stream().map(e -> {
                IQiYi iQiYi = newBaseVideoEntity(httpResponse.getHttpRequest());
                Elements a = e.select("div.site-piclist_pic > a");
                iQiYi.setPlayValue(a.attr("href"));
                iQiYi.setImage("http:" + a.select("img").attr("src"));
                iQiYi.setTitle(a.select("img").attr("title"));
                iQiYi.setPlayInfo(a.select("span.icon-vInfo").text());
                iQiYi.setCategory("动漫");
                return iQiYi;
            }).collect(Collectors.toList());
        };
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
