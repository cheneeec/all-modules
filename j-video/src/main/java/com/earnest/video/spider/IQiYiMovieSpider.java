package com.earnest.video.spider;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.IQiYiMovieCachedVideoService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//@Component
@AllArgsConstructor
public class IQiYiMovieSpider extends AbstractBaseVideoEntitySpider {

    private final IQiYiMovieCachedVideoService iQiYiMovieCachedVideoService;

    private final static String FROM_URL = "https://list.iqiyi.com/www/1/-------------24-1-1-iqiyi--.html";

    private final static String RANGE_REGEX_URL = "http://list.iqiyi.com/www/1/-------------24-${1~12}-1-iqiyi--.html";


    @Override
    protected Consumer<HttpResponseResult<Document>> getCssSelectorPipeline() {
        return httpResponse -> {
            Element element = httpResponse.getContent().body();
            Elements elements = element.select("#widget-tab-0 > div.piclist-scroll.piclist-scroll-h290 > div > div:nth-child(1) > ul > li:not(li.J_videoLi.first_bigImg)");

            List<IQiYi> iQiYis = elements.stream().map(e -> {
                IQiYi iQiYi = new IQiYi();
                iQiYi.setId(id.getAndIncrement());
                iQiYi.setFromUrl(httpResponse.getHttpRequest().getRequestLine().getUri());

                Elements a = e.select("div.site-piclist_pic > a");
                iQiYi.setPlayValue(a.attr("href"));
                iQiYi.setTitle(a.attr("title"));
                iQiYi.setImage(a.select("img").attr("src"));
                iQiYi.setCategory("电影");
                return iQiYi;
            }).collect(Collectors.toList());

            iQiYiMovieCachedVideoService.save(iQiYis);

        };
    }

    @Override
    protected String getRangeRegexUrl() {
        return RANGE_REGEX_URL;
    }

    @Override
    protected String getFromUrl() {
        return FROM_URL;
    }


}
