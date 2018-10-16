package com.earnest.video.service;

import com.earnest.video.entity.Video;
import com.earnest.video.entity.VideoEntity;
import com.earnest.video.spider.persistence.MemoryVideoPersistence;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MemoryVideoService implements VideoService {

    private final Map<String, Video> videoMap;
    private final Map<VideoEntity.Category, List<Video>> categoryTMap;

    public MemoryVideoService(MemoryVideoPersistence<Video> memoryVideoPersistence) {
        this.videoMap = memoryVideoPersistence.getVideoMap();
        this.categoryTMap = memoryVideoPersistence.getCategoryTMap();
    }

    @Override
    public Page<Video> findByCategory(Pageable pageRequest, VideoEntity.Category category) {
        Assert.notNull(pageRequest, "pageRequest is required");
        List<Video> content = categoryTMap.get(category);

        if (content == null) {
            return Page.empty();
        }

        return new PageImpl<>(
                //获得内容
                content.stream().skip(pageRequest.getOffset())
                        .limit(pageRequest.getPageSize())
                        .collect(Collectors.toList()),
                pageRequest, content.size());
    }

    @Override
    public Video get(String id) {
        Assert.hasText(id, "the id is required");
        return videoMap.get(id);
    }

    @Override
    public Page<Video> findAll(Pageable pageRequest) {
        Assert.notNull(pageRequest, "pageRequest is required");
        Collection<Video> values = videoMap.values();
        return new PageImpl<>(
                //获得内容
                values.stream().skip(pageRequest.getOffset())
                        .limit(pageRequest.getPageSize())
                        .collect(Collectors.toList()),
                pageRequest, videoMap.size());
    }
}
