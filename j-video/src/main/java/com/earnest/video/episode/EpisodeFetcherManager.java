package com.earnest.video.episode;

import com.earnest.video.entity.Episode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EpisodeFetcherManager implements EpisodeFetcher {

    private final List<EpisodeFetcher> episodeFetchers = new ArrayList<>();

    public EpisodeFetcherManager() {
        this.episodeFetchers.add(new IQiYiEpisodeFetcher());
    }


    @Override
    public List<Episode> fetch(String url, EpisodePage episodePage) throws IOException {
        for (EpisodeFetcher episodeFetcher : episodeFetchers) {
            if (episodeFetcher.support(url)) {
                log.debug("{} starts to crawl the episodes of url:{}", episodeFetcher.getClass(), url);
                return episodeFetcher.fetch(url, episodePage);
            }
        }
        log.warn("{} dose not support,you need add the implementation of com.earnest.video.episode.EpisodeFetcher", url);
        return null;
    }

    @Override
    public boolean support(String url) {

        return episodeFetchers.stream().map(episodeFetcher -> episodeFetcher.support(url)).reduce((a, b) -> a || b).orElse(false);
    }
}
