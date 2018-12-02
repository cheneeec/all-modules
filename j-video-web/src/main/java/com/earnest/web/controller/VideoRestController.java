package com.earnest.web.controller;


import com.earnest.video.entity.Video;
import com.earnest.web.service.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/api/video")
@AllArgsConstructor
public class VideoRestController {
//http://www.iqiyi.com/v_19rqzi1f7s.html

    private final VideoService videoService;


    @GetMapping("/category/{type:movie|animation}")
    public Page<? extends Video> listAll(@PathVariable String type, Pageable pageRequest) {
        return videoService.findByCategory(pageRequest, Video.Category.getCategory(type));
    }

    @GetMapping("/{id:^[a-z0-9]+$+}")
    public Video get(@PathVariable String id) {
        return videoService.get(id);
    }

    @GetMapping
    public Page<? extends Video> listAll(Pageable pageRequest) {
        return videoService.findAll(pageRequest);
    }



}
