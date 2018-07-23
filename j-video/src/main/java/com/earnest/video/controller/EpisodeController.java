package com.earnest.video.controller;

import com.earnest.video.entity.Episode;
import com.earnest.video.episode.EpisodeFetcher;
import com.earnest.video.episode.EpisodeFetcherManager;
import com.earnest.video.episode.EpisodePage;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/episode")
public class EpisodeController {

    private EpisodeFetcher episodeFetcher = new EpisodeFetcherManager();

    @GetMapping("/query")
    public List<Episode> findEpisodes(@NotBlank String url, Pageable pageRequest) throws IOException {
        return episodeFetcher.fetch(url,
                new EpisodePage(pageRequest.getPageNumber() + 1,
                        pageRequest.getPageSize())
        );

    }

}
