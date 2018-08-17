package com.earnest.video.spider;

import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.IQiYiAnimationCachedVideoService;
import org.springframework.beans.factory.annotation.Autowired;


public class IQiYiAnimationSpider extends AbstractBaseVideoEntitySpider<IQiYi> {

    @Autowired
    private IQiYiAnimationCachedVideoService iQiYiAnimationCachedVideoService;




    /* Element element = Jsoup.parse(httpResponse.getContent()).body();
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
            */

}
