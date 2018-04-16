package com.earnest.video.service;

import com.earnest.video.entity.BaseVideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VideoService<T extends BaseVideoEntity> {
    void save(List<T> entities);

    void save(T entity);


    Page<T> findAll(Pageable pageRequest);

    List<T> findAll();

    T get(Long id);
}
