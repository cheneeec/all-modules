package com.earnest.video.core.episode;

import com.earnest.crawler.proxy.HttpProxyPoolAware;
import com.earnest.video.entity.Episode;
import org.springframework.data.domain.Pageable;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;


public interface EpisodeFetcher extends Closeable, HttpProxyPoolAware {
    /**
     * 对指定的{@code url}进行抓取，并且获得集数。
     *
     * @param url         指定的url
     * @param episodePage 抓取的页面描述。
     * @return 封装成响应的对象。
     */
    List<Episode> fetch(String url, Pageable episodePage) throws IOException;

    boolean support(String url);

}
