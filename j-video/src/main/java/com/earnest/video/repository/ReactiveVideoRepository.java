package com.earnest.video.repository;

import com.earnest.video.entity.Video;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveVideoRepository extends ReactiveMongoRepository<Video, String> {

}
