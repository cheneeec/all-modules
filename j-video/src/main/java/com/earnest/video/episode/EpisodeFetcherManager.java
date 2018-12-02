package com.earnest.video.episode;

import com.earnest.crawler.proxy.HttpProxySupplier;
import com.earnest.video.Manager;
import com.earnest.video.entity.Episode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@CacheConfig(cacheNames = "episode")
public class EpisodeFetcherManager implements EpisodeFetcher, Manager<EpisodeFetcher> {

    private final List<EpisodeFetcher> episodeFetchers = new ArrayList<>(5);


    @Override
    @Cacheable(key = "#url+'['+#episodePage.getPageNumber()+','+#episodePage.getPageSize()+']'")
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
    public void setHttpProxySupplier(HttpProxySupplier httpProxySupplier) {
        episodeFetchers.forEach(s -> s.setHttpProxySupplier(httpProxySupplier));
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
        public void setHttpProxySupplier(HttpProxySupplier httpProxySupplier) {

        }

        @Override
        public void close() throws IOException {

        }
    }
}
