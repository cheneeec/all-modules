package com.earnest.video.core.search;

import com.earnest.crawler.proxy.HttpProxyPoolAware;
import com.earnest.video.entity.Video;
import com.earnest.video.entity.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Closeable;
import java.io.IOException;

public interface PlatformSearcher<T extends Video> extends HttpProxyPoolAware, Closeable {

    Page<T> search(String keyword, Pageable pageRequest) throws IOException;


    Platform getPlatform();
}
