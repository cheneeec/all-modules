package com.earnest.video.core.search;

import com.earnest.video.core.Manager;
import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PlatformSearcherManager extends Manager<PlatformSearcher<? extends BaseVideoEntity>>,PlatformSearcher<BaseVideoEntity> {

    Page<? extends BaseVideoEntity> search(String keyword, Pageable pageRequest, Platform platform) throws IOException;

}
