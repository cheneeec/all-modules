package com.earnest.video.core.search;

import com.alibaba.fastjson.util.IOUtils;
import com.earnest.crawler.core.proxy.HttpProxyPool;
import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.Platform;
import com.earnest.video.exception.UnsupportedPlatformException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Slf4j
public class PlatformSearcherManager implements PlatformSearcher<BaseVideoEntity>, Closeable {

    private final Map<Platform, PlatformSearcher<? extends BaseVideoEntity>> platformSearcherMap;

    @Getter
    @Setter
    private ThreadPoolTaskExecutor threadPool;
    @Getter
    @Setter
    private int ignoreTimeOut = 5; //忽略多少秒后的结果


    public PlatformSearcherManager(HttpClient httpClient, ResponseHandler<String> stringResponseHandler, HttpProxyPool httpProxyPool) {
        platformSearcherMap = initializeSearcher(httpClient, stringResponseHandler, httpProxyPool);
    }


    private Map<Platform, PlatformSearcher<? extends BaseVideoEntity>> initializeSearcher(HttpClient httpClient, ResponseHandler<String> stringResponseHandler, HttpProxyPool httpProxyPool) {
        Map<Platform, PlatformSearcher<? extends BaseVideoEntity>> platformSearcherMap = new LinkedHashMap<>(5);

        platformSearcherMap.put(Platform.IQIYI, new IQiYiPlatformHttpClientSearcher(httpClient, stringResponseHandler, httpProxyPool));


        return Collections.unmodifiableMap(platformSearcherMap);
    }

    /**
     * 并发搜索每个平台，将获取到的内容进行归总。
     *
     * @param keyword
     * @param pageRequest
     * @return
     * @throws IOException
     */
    @Override
    public Page<BaseVideoEntity> search(String keyword, Pageable pageRequest) throws IOException {




        return null;
    }

    @SuppressWarnings("unchecked")
    public Page<BaseVideoEntity> search(String keyword, Pageable pageRequest, Platform platform) throws IOException {
        PlatformSearcher<? extends BaseVideoEntity> platformSearcher = platformSearcherMap.get(platform);

        if (platformSearcher != null) {
            return (Page<BaseVideoEntity>) platformSearcher.search(keyword, pageRequest);
        }

        throw new UnsupportedPlatformException(platform.toString() + " Platform does not support or is not specified");
    }


    @Override
    public void close() {
        try {
            platformSearcherMap.values().
                    stream()
                    .filter(s -> s instanceof Closeable)
                    .map(a -> (Closeable) a)
                    .forEach(IOUtils::close);
        } finally {
            threadPool.shutdown();
        }
    }

    @Override
    public void setHttpProxyPool(HttpProxyPool httpProxyPool) {
        platformSearcherMap.values().forEach(s -> s.setHttpProxyPool(httpProxyPool));
    }


}
