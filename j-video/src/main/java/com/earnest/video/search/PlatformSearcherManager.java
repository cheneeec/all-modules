package com.earnest.video.search;

import com.earnest.crawler.core.proxy.HttpProxyPool;
import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.Platform;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

public class PlatformSearcherManager implements PlatformSearcher<BaseVideoEntity>, Closeable {


    private final List<PlatformSearcher<? extends BaseVideoEntity>> platformSearchers = new ArrayList<>();


    private final Map<Platform, PlatformSearcher<? extends BaseVideoEntity>> platformSearcherMap;

    public PlatformSearcherManager(HttpClient httpClient, ResponseHandler<String> stringResponseHandler, HttpProxyPool httpProxyPool) {
        platformSearcherMap = initialize(httpClient, stringResponseHandler, httpProxyPool);
    }

    private Map<Platform, PlatformSearcher<? extends BaseVideoEntity>> initialize(HttpClient httpClient, ResponseHandler<String> stringResponseHandler, HttpProxyPool httpProxyPool) {
        Map<Platform, PlatformSearcher<? extends BaseVideoEntity>> platformSearcherMap = new LinkedHashMap<>(5);
        platformSearcherMap.put(Platform.IQIYI, new IQiYiPlatformHttpClientSearcher(httpClient, stringResponseHandler, httpProxyPool));
        return Collections.unmodifiableMap(platformSearcherMap);
    }

    @Override
    public Page<BaseVideoEntity> search(String keyword, Pageable pageRequest) throws IOException {
        return null;
    }


    @Override
    public void close() throws IOException {

    }

}
