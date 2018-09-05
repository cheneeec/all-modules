package com.earnest.video.service;

import com.earnest.video.entity.Episode;
import com.earnest.video.episode.EpisodeFetcher;
import com.earnest.video.episode.EpisodePage;
import com.earnest.video.exception.UnknownException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeFetcher episodeFetcher;


    @Override
    public List<Episode> findAll(String url, int page, int size) {
        Assert.hasText(url, "url is empty or null");
        try {
            return episodeFetcher.fetch(url, new EpisodePage(page, size));
        } catch (IOException e) {
            throw new UnknownException(e);
        }
    }
}
