package com.earnest.video.service;

import com.earnest.video.entity.VideoEntity;
import com.earnest.video.entity.Video;
import com.earnest.video.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class MongodbVideoService implements BasicQueryAndPersistenceVideoService<Video> {

    private final VideoRepository videoRepository;


    @Override
    public void save(List<Video> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            videoRepository.saveAll(entities);
        }
    }

    @Override
    public void save(Video entity) {
        if (entity == null) {
            return;
        }
        Assert.isTrue(entity.getId() != null, "id is not null");
        videoRepository.save(entity);


    }

    @Override
    public Page<Video> findAll(Pageable pageRequest) {
        return videoRepository.findAll(Optional.ofNullable(pageRequest).orElse(Pageable.unpaged()));
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public Video get(String id) {
        Assert.notNull(id, "id is required");
        return videoRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Video> findByCategory(Pageable pageRequest, VideoEntity.Category category) {
        Assert.notNull(category, "category is required");
        pageRequest = Optional.ofNullable(pageRequest).orElse(Pageable.unpaged());
        return videoRepository.findByCategory(pageRequest, category);
    }

    @Override
    public long count() {
        return videoRepository.count();
    }
}
