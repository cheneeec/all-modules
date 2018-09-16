package com.earnest.video.core.episode;

import com.earnest.crawler.core.proxy.HttpProxyPool;
import com.earnest.video.core.Manager;
import com.earnest.video.entity.Episode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class EpisodeFetcherManager implements EpisodeFetcher, Manager<EpisodeFetcher> {

    private final List<EpisodeFetcher> episodeFetchers = new ArrayList<>(5);


    @Override
    @SuppressWarnings("unchecked")
    public List<Episode> fetch(String url, Pageable episodePage) {

        return (List<Episode>) episodeFetchers.stream()
                .filter(episodeFetcher -> episodeFetcher.support(url))
                .map(invokeFetcherMethod(url, episodePage))
                .findAny()
                .orElseGet(() -> {
                    log.warn("{} dose not support,you need add the implementation of {}", url, EpisodeFetcher.class);
                    return Collections.emptyList();
                });
    }

    private static Function<EpisodeFetcher, List<?>> invokeFetcherMethod(String url, Pageable episodePage) {
        return episodeFetcher -> {
            try {
                log.debug("{} starts to crawl the episodes of url:{}", episodeFetcher.getClass(), url);
                return episodeFetcher.fetch(url, episodePage);
            } catch (IOException e) {
                log.error("An error occurred while getting the episodes by class:{},error:{}", episodeFetcher.getClass(), e.getMessage());
            }
            return Collections.emptyList();
        };
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
}
