package com.earnest.video.core.search;

import com.earnest.video.core.Manager;
import com.earnest.video.entity.Video;
import com.earnest.video.entity.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface PlatformSearcherManager extends Manager<PlatformSearcher<? extends Video>>,PlatformSearcher<Video> {

    Page<? extends Video> search(String keyword, Pageable pageRequest, Platform platform) throws IOException;

}
