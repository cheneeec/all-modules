package com.earnest.video.controller;


import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.BasicQueryAndPersistenceVideoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class VideoController {

    private  BasicQueryAndPersistenceVideoService<IQiYi> iQiYiVideoService;

    @GetMapping
    public Page<? extends BaseVideoEntity> listAll(Pageable pageRequest) {
        return iQiYiVideoService.findAll(pageRequest);
    }

    @GetMapping("/list")
    public List<? extends BaseVideoEntity> listAll() {
        return iQiYiVideoService.findAll();
    }

    @GetMapping("/{id:\\d+}")
    public BaseVideoEntity get(@PathVariable long id) {
        return iQiYiVideoService.get(id);
    }

}
