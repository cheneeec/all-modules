package com.earnest.video.service;

import com.earnest.video.entity.BaseVideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractCachedQueryAndPersistenceVideoService<T extends BaseVideoEntity> implements BasicQueryAndPersistenceVideoService<T> {

    private final Map<Long, T> cachedMap;

    public AbstractCachedQueryAndPersistenceVideoService(Map<Long, T> cachedMap) {
        Assert.notNull(cachedMap, "the cachedMap is required");
        this.cachedMap = cachedMap;
    }

    public AbstractCachedQueryAndPersistenceVideoService() {
        this(new ConcurrentHashMap<>());
    }


    @Override
    public void save(List<T> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            cachedMap.putAll(entities.stream().collect(Collectors.toMap(BaseVideoEntity::getId, e -> e)));
        }
    }

    @Override
    public void save(T entity) {
        if (entity != null) {
            cachedMap.put(entity.getId(), entity);
        }

    }

    @Override
    public Page<T> findAll(Pageable pageRequest) {
        Assert.notNull(pageRequest, "pageRequest is null");
        Collection<T> values = cachedMap.values();

        return new PageImpl<>(
                //获得内容
                values.stream().skip(pageRequest.getOffset())
                        .limit(pageRequest.getPageSize())
                        .collect(Collectors.toList()),
                pageRequest, cachedMap.size());
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(cachedMap.values());
    }

    @Override
    public T get(Long id) {
        Assert.notNull(id, "the id is required");
        return cachedMap.get(id);
    }

    @Override
    public Page<T> findByCategory(Pageable pageRequest, BaseVideoEntity.Category category) {
        return null;
    }
}
