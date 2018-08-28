package com.earnest.video.episode;

import com.earnest.video.entity.Episode;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;


public interface EpisodeFetcher  extends Closeable {
    /**
     * 对指定的{@code url}进行抓取，并且获得集数。
     *
     * @param url         指定的url
     * @param episodePage 抓取的页面描述。
     * @return 封装成响应的对象。
     */
    List<Episode> fetch(String url, EpisodePage episodePage) throws IOException;

    boolean support(String url);
}
