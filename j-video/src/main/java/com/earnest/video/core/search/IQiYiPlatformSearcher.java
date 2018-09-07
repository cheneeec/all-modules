package com.earnest.video.core.search;

import com.earnest.crawler.core.Browser;
import com.earnest.crawler.core.proxy.HttpProxyPoolSettingSupport;
import com.earnest.video.entity.IQiYi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @deprecated 使用 {@link IQiYiPlatformHttpClientSearcher}替代。
 */
@Slf4j
@Deprecated
public class IQiYiPlatformSearcher  extends HttpProxyPoolSettingSupport implements PlatformSearcher<IQiYi> {

    private static final String URL_STRING = "http://so.iqiyi.com/so/q_%s";


    @Override
    public Page<IQiYi> search(String keyword, Pageable pageRequest) throws IOException {
        Assert.hasText(keyword, "keyword is empty or null");

        String url = String.format(URL_STRING, UriUtils.encode(keyword, Charset.defaultCharset().displayName()));

        Element element = Jsoup.connect(url)
                .data("source", "suggest")
                .userAgent(Browser.GOOGLE.userAgent())
                .timeout(5000)
                .validateTLSCertificates(false)
                .get().body();

        log.trace("connect {} succeeded", url);

        Elements elements = element.select("div.mod_result ul.mod_result_list li.list_item");


        return new PageImpl<>(elements.stream().map(mapToIQiYiEntity()).collect(Collectors.toList()), pageRequest, 20);


    }

    private static Function<Element, IQiYi> mapToIQiYiEntity() {
        Date now = Calendar.getInstance().getTime();
        return e -> {
            IQiYi iQiYi = new IQiYi();
            Elements img = e.select("img");
            //分类
            iQiYi.setCategory(e.attr("data-widget-searchlist-catageory"));
            //albumid不一定有用
            iQiYi.setAlbumId(e.attr("data-widget-searchlist-albumid"));
            iQiYi.setImage(img.attr("abs:src"));
            iQiYi.setTitle(img.attr("title"));
            //id没用
            iQiYi.setId(RandomUtils.nextLong());
            iQiYi.setCollectTime(now);
            //播放信息
            iQiYi.setPlayInfo(e.select("span.icon-vInfo").text());
            //播放地址
            iQiYi.setPlayValue(e.select("a.figure").attr("href"));

            return iQiYi;

        };
    }
}
