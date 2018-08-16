package com.earnest.video.spider;

import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.IQiYiMovieCachedVideoService;
import org.springframework.beans.factory.annotation.Autowired;




public class IQiYiMovieSpider extends AbstractBaseVideoEntitySpider<IQiYi>{

    @Autowired
    private IQiYiMovieCachedVideoService iQiYiMovieCachedVideoService;




    /*
    * Element element = Jsoup.parse(httpResponse.getContent()).body();
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
            */

}
