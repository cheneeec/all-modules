package com.earnest.video.controller;

import com.earnest.video.entity.Episode;
import com.earnest.video.episode.EpisodeFetcher;
import com.earnest.video.episode.EpisodeFetcherManager;
import com.earnest.video.episode.EpisodePage;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/episode")
public class EpisodeController {

    private final EpisodeFetcher episodeFetcher = new EpisodeFetcherManager();

    @GetMapping("/query")
    public List<Episode> findEpisodes(@NotBlank String url, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int size) throws IOException {
        return episodeFetcher.fetch(url, new EpisodePage(page, size));
    }

}
