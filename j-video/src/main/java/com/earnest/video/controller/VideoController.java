package com.earnest.video.controller;


import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.BaseVideoEntity.Category;
import com.earnest.video.entity.Video;
import com.earnest.video.service.BasicQueryAndPersistenceVideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/api/video")
@AllArgsConstructor
public class VideoController {
//http://www.iqiyi.com/v_19rqzi1f7s.html

    private final BasicQueryAndPersistenceVideoService<Video> videoService;


    @GetMapping("/{type:movie|animation}")
    public Page<? extends BaseVideoEntity> listAll(@PathVariable String type, Pageable pageRequest) {
        return videoService.findByCategory(pageRequest, Category.getCategory(type));
    }

    @GetMapping("/{id:\\d+}")
    public BaseVideoEntity get(@PathVariable long id) {
        return videoService.get(id);
    }


}
