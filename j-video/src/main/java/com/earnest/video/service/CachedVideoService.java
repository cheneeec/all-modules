package com.earnest.video.service;

import com.earnest.video.entity.BaseVideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public  class CachedVideoService<T extends BaseVideoEntity> implements BasicQueryAndPersistenceVideoService<T> {

    private final Map<Long, T> videoMap;

    private final Map<BaseVideoEntity.Category, List<T>> categoryTMap;

    public CachedVideoService(Map<Long, T> videoMap) {
        Assert.notNull(videoMap, "the videoMap is required");
        this.videoMap = videoMap;
        this.categoryTMap = new ConcurrentHashMap<>();
    }

    public CachedVideoService() {
        this(new ConcurrentSkipListMap<>());
    }


    @Override
    public void save(List<T> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            categoryTMap.putAll(entities.stream()
                    .filter(Objects::nonNull)
                    .peek(e -> videoMap.put(e.getId(), e))
                    .collect(Collectors.groupingBy(BaseVideoEntity::getCategory)));
        }
    }

    @Override
    public void save(T entity) {
        if (entity != null) {
            videoMap.put(entity.getId(), entity);
            List<T> entities = new ArrayList<>();
            entities = categoryTMap.getOrDefault(entity.getCategory(), entities);
            entities.add(entity);

        }

    }

    @Override
    public Page<T> findAll(Pageable pageRequest) {
        Assert.notNull(pageRequest, "pageRequest is required");
        Collection<T> values = videoMap.values();
        return new PageImpl<>(
                //获得内容
                values.stream().skip(pageRequest.getOffset())
                        .limit(pageRequest.getPageSize())
                        .collect(Collectors.toList()),
                pageRequest, videoMap.size());
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(videoMap.values());
    }

    @Override
    public T get(Long id) {
        Assert.notNull(id, "the id is required");
        return videoMap.get(id);
    }

    @Override
    public Page<T> findByCategory(Pageable pageRequest, BaseVideoEntity.Category category) {
        Assert.notNull(pageRequest, "pageRequest is required");
        return new PageImpl<>(
                //获得内容
                categoryTMap.get(category).stream().skip(pageRequest.getOffset())
                        .limit(pageRequest.getPageSize())
                        .collect(Collectors.toList()),
                pageRequest, videoMap.size());

    }
}
