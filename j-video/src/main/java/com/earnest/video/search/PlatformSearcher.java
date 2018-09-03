package com.earnest.video.search;

import com.earnest.video.entity.BaseVideoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PlatformSearcher<T extends BaseVideoEntity> {
    Page<T> search(String keyword, Pageable pageRequest) throws IOException;
}
