package com.earnest.video.controller;

import com.earnest.video.core.search.PlatformSearcher;
import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.Platform;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RequestMapping("/v1/api/video")
@RestController
@AllArgsConstructor
public class SearchController {

    private final PlatformSearcher<? extends BaseVideoEntity> platformSearcher;

    @GetMapping("/search")
    public Page<? extends BaseVideoEntity> search(@NotBlank @RequestParam(name = "q") String keyword,
                                                  @PageableDefault(page = 1) Pageable pageRequest,
                                                  Platform platform) throws IOException {

        return platformSearcher.search(keyword, pageRequest);
    }

}
