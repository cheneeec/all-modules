package com.earnest.video.episode;

import com.earnest.video.entity.Episode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EpisodeFetcherManager implements EpisodeFetcher {

    private final List<EpisodeFetcher> episodeFetchers = new ArrayList<>();

    public EpisodeFetcherManager(CloseableHttpClient httpClient) {
        episodeFetchers.add(new IQiYiEpisodeFetcher(httpClient, stringResponseHandler));
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<Episode> fetch(String url, EpisodePage episodePage) {

        return (List<Episode>) episodeFetchers.stream()
                .filter(episodeFetcher -> episodeFetcher.support(url))
                .map(episodeFetcher -> {
                    try {
                        log.debug("{} starts to crawl the episodes of url:{}", episodeFetcher.getClass(), url);
                        return episodeFetcher.fetch(url, episodePage);
                    } catch (IOException e) {
                        log.error("An error occurred while getting the episodes by class:{},error:{}", episodeFetcher.getClass(), e.getMessage());
                    }
                    return Collections.emptyList();
                }).findAny().orElseGet(() -> {
                    log.warn("{} dose not support,you need add the implementation of {}", url, EpisodeFetcher.class);
                    return Collections.emptyList();
                });
    }

    @Override
    public boolean support(String url) {
        return episodeFetchers.stream()
                .map(episodeFetcher -> episodeFetcher.support(url)).findAny().orElse(false);
    }

    @Override
    public void close() throws IOException {
        for (EpisodeFetcher episodeFetcher : episodeFetchers) {
            episodeFetcher.close();
        }
    }
}
