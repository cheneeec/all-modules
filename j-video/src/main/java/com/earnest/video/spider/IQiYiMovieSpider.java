package com.earnest.video.spider;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.BasicQueryAndPersistenceVideoService;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class IQiYiMovieSpider extends AbstractBaseVideoEntitySpider {

    private final BasicQueryAndPersistenceVideoService iQiYiMovieCachedVideoService;

    private final static String FROM_URL = "https://list.iqiyi.com/www/1/-------------24-1-1-iqiyi--.html";

    private final static String RANGE_REGEX_URL = "https://list.iqiyi.com/www/1/-------------24-${1~30}-1-iqiyi--.html";



    @Override
    protected Function<HttpResponseResult<Document>, List<BaseVideoEntity>> pipe() {
        return httpResponse -> {
            Element element = httpResponse.getContent().body();
            Elements elements = element.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");

            return elements.stream().map(e -> {
                IQiYi iQiYi = new IQiYi();
                iQiYi.setFromUrl(httpResponse.getHttpRequest().getRequestLine().getUri());

                Elements a = e.select("div.site-piclist_pic > a");
                iQiYi.setPlayValue(a.attr("href"));
                iQiYi.setTitle(a.attr("title"));
                iQiYi.setImage(a.select("img").attr("src"));
                iQiYi.setCategory(BaseVideoEntity.Category.MOVIE);
                return iQiYi;
            }).collect(Collectors.toList());
        };
    }

    @Override
    protected Consumer<List<BaseVideoEntity>> consumer() {
        return iQiYiMovieCachedVideoService::save;
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
