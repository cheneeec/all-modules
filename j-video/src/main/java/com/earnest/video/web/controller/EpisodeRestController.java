package com.earnest.video.web.controller;

import com.earnest.video.core.episode.EpisodeFetcher;
import com.earnest.video.entity.Episode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/episode")
@AllArgsConstructor
public class EpisodeRestController {

    private final EpisodeFetcher episodeFetcher;

    @GetMapping(value = "/query")
    public List<Episode> findEpisodes(@NotBlank String url, @PageableDefault(page = 1, size = 50) Pageable page) throws IOException {
        return episodeFetcher.fetch(url, page);
    }


}
