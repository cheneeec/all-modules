package com.earnest.video.episode;

import com.earnest.video.entity.Episode;

import java.util.List;


@FunctionalInterface
public interface EpisodeFetcher {
    /**
     * 对指定的{@code url}进行抓取，并且获得集数。
     *
     * @param url  指定的url
     * @param page 抓取的页数。
     * @param size 抓取的页面尺寸。
     * @return 封装成响应的对象。
     */
    List<Episode> fetch(String url, int page, int size);
}
