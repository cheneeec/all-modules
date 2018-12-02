package com.earnest.web.spider;

import com.earnest.crawler.HttpResponseResult;
import com.earnest.video.entity.Platform;
import com.earnest.video.entity.Video;
import com.earnest.web.spider.persistence.VideoPersistence;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.earnest.video.util.VideoUtils.isPlayTime;

@AllArgsConstructor
public class IQiYiMovieSpider extends AbstractBaseVideoEntitySpider {

    private final VideoPersistence iQiYiMovieCachedVideoService;

    private final static String FROM_URL = "https://list.iqiyi.com/www/1/-------------24-1-1-iqiyi--.html";

    private final static String RANGE_REGEX_URL = "https://list.iqiyi.com/www/1/-------------24-${1~30}-1-iqiyi--.html";


    @Override
    protected Function<HttpResponseResult<Document>, List<Video>> pipe() {

        return httpResponse -> {
            Element element = httpResponse.getContent().body();
            Elements elements = element.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");

            return elements.stream().map(e -> {
                Video iQiYi = new Video();

                iQiYi.setPlatform(Platform.IQIYI);
                iQiYi.setFromUrl(httpResponse.getHttpRequest().getRequestLine().getUri());
                Elements a = e.select("div.site-piclist_pic > a");
                iQiYi.setRawValue(a.attr("href"));

                iQiYi.setTitle(a.attr("title"));
                iQiYi.setImage(a.select("img").attr("src"));
                iQiYi.setCategory(Video.Category.MOVIE);
                //body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li:nth-child(1) > div.site-piclist_pic > a >
                String playInfo = a.select("div > div > p > span").text();
                iQiYi.setPlayInfo(playInfo);
                iQiYi.setSingle(isPlayTime(playInfo));

                return iQiYi;
            }).collect(Collectors.toList());
        };
    }

    @Override
    protected Consumer<List<? extends Video>> consumer() {
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
