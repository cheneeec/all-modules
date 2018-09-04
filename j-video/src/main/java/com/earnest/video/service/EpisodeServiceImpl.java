package com.earnest.video.service;

import com.earnest.video.entity.Episode;
import com.earnest.video.episode.EpisodeFetcherManager;
import com.earnest.video.episode.EpisodePage;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeFetcherManager episodeFetcher;

    public EpisodeServiceImpl(CloseableHttpClient httpClient) {
        this.episodeFetcher = new EpisodeFetcherManager(httpClient);
    }


    @Override
    public List<Episode> findAll(String url, int page, int size) {
        Assert.hasText(url, "url is empty or null");

        return episodeFetcher.fetch(url, new EpisodePage(page, size));
    }
}
