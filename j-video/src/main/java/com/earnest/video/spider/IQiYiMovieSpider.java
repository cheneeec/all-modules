package com.earnest.video.spider;

import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.IQiYiMovieCachedVideoService;
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
public class IQiYiMovieSpider extends AbstractBaseVideoEntitySpider<IQiYi> implements CommandLineRunner {

    @Autowired
    private IQiYiMovieCachedVideoService iQiYiMovieCachedVideoService;

    public IQiYiMovieSpider() {
        super("http://www.iqiyi.com/dianying_new/i_list_paihangbang.html", null, 1);
    }

    @Override
    protected Pipeline<List<IQiYi>> createPipeline() {
        return httpResponse -> {
            Element element = Jsoup.parse(httpResponse.getContent()).body();
            Elements elements = element.select("#widget-tab-0 > div.piclist-scroll.piclist-scroll-h290 > div > div:nth-child(1) > ul > li:not(li.J_videoLi.first_bigImg)");
            final int[] i = {1};
            return elements.stream().map(e -> {
                IQiYi iQiYi = newBaseVideoEntity(httpResponse.getHttpRequest());
                Elements a = e.select("div.site-piclist_pic > a");
                iQiYi.setPlayValue(a.attr("href"));
                iQiYi.setTitle(a.attr("title"));
                iQiYi.setImage(a.select("img").attr("src"));
                iQiYi.setCategory("电影");
                return iQiYi;
            }).collect(Collectors.toList());
        };
    }

    @Override
    protected Consumer<List<IQiYi>> getSpiderConsumer() {
        return o -> iQiYiMovieCachedVideoService.save(o);
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
