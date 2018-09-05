package com.earnest.video.service;

import com.earnest.video.entity.BaseVideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    Page<? extends BaseVideoEntity> search(String keyword, Pageable pageRequest);
}
