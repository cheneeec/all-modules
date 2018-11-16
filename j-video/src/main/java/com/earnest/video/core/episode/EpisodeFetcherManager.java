package com.earnest.video.core.episode;

import com.earnest.crawler.proxy.HttpProxyPool;
import com.earnest.video.core.Manager;
import com.earnest.video.entity.Episode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EpisodeFetcherManager implements EpisodeFetcher, Manager<EpisodeFetcher> {

    private final List<EpisodeFetcher> episodeFetchers = new ArrayList<>(5);


    @Override
    @SuppressWarnings("unchecked")
    public List<Episode> fetch(String url, Pageable episodePage) throws IOException {

        EpisodeFetcher fetcher = episodeFetchers.stream()
                .filter(episodeFetcher -> episodeFetcher.support(url))
                .findAny()
                .orElseGet(() -> {
                    log.warn("{} dose not support,you need add the implementation of {}", url, EpisodeFetcher.class);
                    return EmptyEpisodeFetcher.INSTANCE;
                });

        return fetcher.fetch(url, episodePage);

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

    @Override
    public void setHttpProxyPool(HttpProxyPool httpProxyPool) {
        episodeFetchers.forEach(s -> s.setHttpProxyPool(httpProxyPool));
    }


    @Override
    public void addWork(EpisodeFetcher episodeFetcher) {
        Assert.notNull(episodeFetcher, "episodeFetcher is null");
        episodeFetchers.add(episodeFetcher);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    static class EmptyEpisodeFetcher implements EpisodeFetcher {

       static final EmptyEpisodeFetcher INSTANCE = new EmptyEpisodeFetcher();

        @Override
        public List<Episode> fetch(String url, Pageable episodePage) throws IOException {
            return Collections.emptyList();
        }

        @Override
        public boolean support(String url) {
            return true;
        }

        @Override
        public void setHttpProxyPool(HttpProxyPool httpProxyPool) {

        }

        @Override
        public void close() throws IOException {

        }
    }
}
