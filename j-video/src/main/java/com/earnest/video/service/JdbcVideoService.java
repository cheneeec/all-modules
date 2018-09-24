package com.earnest.video.service;

import com.earnest.video.entity.BaseVideoEntity;
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
public class JdbcVideoService implements BasicQueryAndPersistenceVideoService<Video> {

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
    public Video get(Long id) {
        Assert.notNull(id, "id is required");
        return videoRepository.getOne(id);
    }

    @Override
    public Page<Video> findByCategory(Pageable pageRequest, BaseVideoEntity.Category category) {
        Assert.notNull(category, "category is required");
        pageRequest = Optional.ofNullable(pageRequest).orElse(Pageable.unpaged());
        return videoRepository.findByCategory(pageRequest, category);
    }
}
