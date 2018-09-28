package com.earnest.video.repository;

import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, Long> {

    Page<Video> findByCategory(Pageable pageRequest, BaseVideoEntity.Category category);

}
