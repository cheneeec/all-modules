package com.earnest.video.controller;

import com.earnest.video.entity.Episode;
import com.earnest.video.service.EpisodeService;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/episode")
@AllArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    @GetMapping("/query")
    public List<Episode> findEpisodes(@NotBlank String url, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int size) {
        return episodeService.findAll(url, page, size);
    }

}
