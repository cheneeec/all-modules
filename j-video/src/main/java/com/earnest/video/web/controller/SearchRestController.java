package com.earnest.video.web.controller;

import com.earnest.video.core.search.PlatformSearcherManager;
import com.earnest.video.entity.Video;
import com.earnest.video.entity.Platform;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RequestMapping("/v1/api/video")
@RestController
@AllArgsConstructor
public class SearchRestController {

    private final PlatformSearcherManager platformSearcherManager;

    @GetMapping("/search")
    public Page<? extends Video> search(@RequestParam(name = "q") String keyword,
                                        Pageable pageRequest,
                                        Platform platform) throws IOException {
        return platformSearcherManager.search(keyword, pageRequest, platform);
    }

}
