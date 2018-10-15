package com.earnest.video.service;

import com.earnest.video.entity.Video;
import com.earnest.video.entity.VideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideoService  {

    Page<Video> findByCategory(Pageable pageRequest, VideoEntity.Category category);

    Video get(String id);

    Page<Video> findAll(Pageable pageRequest);
}
