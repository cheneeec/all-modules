package com.earnest.video.controller;


import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.service.BasicQueryAndPersistenceVideoService;
import com.earnest.video.service.IQiYiAnimationCachedVideoService;
import com.earnest.video.service.IQiYiMovieCachedVideoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/video")
@SuppressWarnings("unchecked")
public class VideoController {
//http://www.iqiyi.com/v_19rqzi1f7s.html
    private final Map<String, BasicQueryAndPersistenceVideoService> videoServiceMap;


    public VideoController(IQiYiMovieCachedVideoService movieVideoService, IQiYiAnimationCachedVideoService animationVideoService) {
        this.videoServiceMap = initializeVideoServiceMap(movieVideoService, animationVideoService);
    }


    private Map<String, BasicQueryAndPersistenceVideoService> initializeVideoServiceMap(IQiYiMovieCachedVideoService movieVideoService, IQiYiAnimationCachedVideoService animationVideoService) {
        Map<String, BasicQueryAndPersistenceVideoService> tempMap = new HashMap<>();
        tempMap.put("movie", movieVideoService);
        tempMap.put("animation", animationVideoService);
        return Collections.unmodifiableMap(tempMap);
    }

    @GetMapping("/{type:movie|animation}")
    public Page<? extends BaseVideoEntity> listAll(@PathVariable String type, Pageable pageRequest) {
        return videoServiceMap.get(type).findAll(pageRequest);
    }

    @GetMapping("/{id:\\d+}")
    public BaseVideoEntity get(@PathVariable long id, @PathVariable String type) {
        return videoServiceMap.get(type).get(id);
    }


}
