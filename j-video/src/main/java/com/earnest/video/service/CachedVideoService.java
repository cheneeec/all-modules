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
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class CachedVideoService<T extends BaseVideoEntity> implements BasicQueryAndPersistenceVideoService<T> {

    private final Map<Long, T> videoMap;

    private final Map<BaseVideoEntity.Category, List<T>> categoryTMap;

    private final AtomicLong idLong = new AtomicLong(1000);

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
            //将结果按照id添加
            Map<BaseVideoEntity.Category, List<T>> videosByCategory = entities.stream()
                    .filter(Objects::nonNull)
                    .peek(e -> {//当没有ID时为其设置ID
                        if (e.getId() == null) {
                            e.setId(idLong.incrementAndGet());
                        }
                    })
                    .peek(e -> videoMap.put(e.getId(), e))
                    .collect(Collectors.groupingBy(BaseVideoEntity::getCategory));

            //将结果进行分类添加
            videosByCategory.keySet()
                    .forEach(c ->
                            categoryTMap.compute(c, ((category, ts) -> {
                                ts = Optional.ofNullable(ts).orElse(new ArrayList<>());
                                ts.addAll(videosByCategory.get(category));
                                return ts;
                            }))
                    );

        }
    }

    @Override
    public void save(T entity) {
        if (entity != null) {
            if (entity.getId() == null) {
                entity.setId(idLong.incrementAndGet());
            }

            videoMap.put(entity.getId(), entity);

            categoryTMap.compute(entity.getCategory(), ((category, ts) -> {
                ts = Optional.ofNullable(ts).orElse(new ArrayList<>());
                ts.add(entity);
                return ts;
            }));

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
        List<T> content = categoryTMap.get(category);
        if (content == null) {
            return Page.empty();
        }

        return new PageImpl<>(
                //获得内容
                content.stream().skip(pageRequest.getOffset())
                        .limit(pageRequest.getPageSize())
                        .collect(Collectors.toList()),
                pageRequest, videoMap.size());

    }

}
