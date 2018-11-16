package com.earnest.video.spider;

import com.earnest.crawler.HttpResponseResult;
import com.earnest.video.entity.VideoEntity;
import com.earnest.video.entity.IQiYi;
import com.earnest.video.spider.persistence.VideoPersistence;
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
public class IQiYiAnimationSpider extends AbstractBaseVideoEntitySpider {

    private final VideoPersistence iQiYiAnimationCachedVideoService;

    private final static String FROM_URL = "https://list.iqiyi.com/www/4/-------------4-1-1-iqiyi--.html";

    private final static String RANGE_REGEX_URL = "https://list.iqiyi.com/www/4/-------------4-${1~30}-1-iqiyi--.html";


    @Override
    protected Function<HttpResponseResult<Document>, List<VideoEntity>> pipe() {
        return httpResponse -> {
            Element element = httpResponse.getContent().body();
            Elements elements = element.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");
            return elements.stream().map(li -> {
                IQiYi iQiYi = new IQiYi();
                iQiYi.setFromUrl(httpResponse.getHttpRequest().getRequestLine().getUri());

                Elements a = li.select("div.site-piclist_pic > a");
                iQiYi.setPlayValue(a.attr("href"));
                iQiYi.setImage("http:" + a.select("img").attr("src"));
                iQiYi.setTitle(a.select("img").attr("title"));

                String playInfo =a.select("span.icon-vInfo").text() ;
                iQiYi.setPlayInfo(playInfo);
                iQiYi.setSingle(isPlayTime(playInfo));

                iQiYi.setCategory(VideoEntity.Category.ANIMATION);
                iQiYi.setAlbumId(a.attr("data-qidanadd-albumid"));

                iQiYi.setVideoInfo(li.select("div.site-piclist_info > div.role_info").text());


                return iQiYi;
            }).collect(Collectors.toList());

        };
    }

    @Override
    protected Consumer<List<? extends VideoEntity>> consumer() {
        return iQiYiAnimationCachedVideoService::save;
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
