package com.earnest.video.core.search;

import com.alibaba.fastjson.util.IOUtils;
import com.earnest.crawler.core.proxy.HttpProxyPool;
import com.earnest.video.entity.Platform;
import com.earnest.video.entity.VideoEntity;
import com.earnest.video.exception.UnsupportedPlatformException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class DefaultPlatformSearcherManager implements PlatformSearcherManager {

    private final Map<Platform, PlatformSearcher<? extends VideoEntity>> platformSearcherMap = new LinkedHashMap<>(5);

    @Getter
    private CompletionService<Page<? extends VideoEntity>> completionService;

    @Getter
    @Setter
    private Integer ignoreSecondTimeOut = 5; //忽略多少秒后的结果


    public DefaultPlatformSearcherManager(Integer ignoreSecondTimeOut, Executor executor) {
        Assert.notNull(executor, "executor is required");
        this.ignoreSecondTimeOut = Optional.ofNullable(ignoreSecondTimeOut).orElse(5);
        this.completionService = new ExecutorCompletionService<>(executor);
    }

    public DefaultPlatformSearcherManager(Executor executor) {
        this(null, executor);
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
    public Page<VideoEntity> search(String keyword, Pageable pageRequest) throws IOException {


        List<? extends Page<? extends VideoEntity>> results = platformSearcherMap.values()
                .stream()
                .map(search -> (Callable<Page<? extends VideoEntity>>) () -> search.search(keyword, pageRequest))
                .map(completionService::submit) //执行
                .map(futureGetIgnoreError())//取结果
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //收集
        long totalElements = 0L;

        List<VideoEntity> content = new ArrayList<>(pageRequest.getPageSize() * results.size());


        for (Page<? extends VideoEntity> baseVideoEntities : results) {
            totalElements += baseVideoEntities.getTotalElements();
            content.addAll(baseVideoEntities.getContent());
        }


        return new PageImpl<>(content, pageRequest, totalElements);
    }


    @Override
    public Platform getPlatform() {
        return null;
    }

    private Function<Future<Page<? extends VideoEntity>>, ? extends Page<? extends VideoEntity>> futureGetIgnoreError() {
        return f -> {
            try {
                return f.get(ignoreSecondTimeOut, TimeUnit.SECONDS);
            } catch (Exception e) { //发生错误时忽略
                if (log.isDebugEnabled() && e instanceof TimeoutException) {
                    log.debug("A task timed out has been ignored,error:{}", e.getMessage());
                }
                f.cancel(true);
                return null;
            }
        };
    }


    @Override
    @SuppressWarnings("unchecked")
    public Page<VideoEntity> search(String keyword, Pageable pageRequest, Platform platform) throws IOException {

        if (platform == null) {
            return search(keyword, pageRequest);
        }

        PlatformSearcher<? extends VideoEntity> platformSearcher = platformSearcherMap.get(platform);

        if (platformSearcher != null) {
            return (Page<VideoEntity>) platformSearcher.search(keyword, pageRequest);
        }

        throw new UnsupportedPlatformException(platform + " Platform does not support or is not specified");
    }


    @Override
    public void close() {
        try {
            platformSearcherMap.values()
                    .forEach(IOUtils::close);
        } finally {
            if (completionService instanceof ExecutorService) {
                ((ExecutorService) completionService).shutdown();
            }

        }
    }

    @Override
    public void setHttpProxyPool(HttpProxyPool httpProxyPool) {
        platformSearcherMap.values().forEach(s -> s.setHttpProxyPool(httpProxyPool));
    }


    @Override
    public void addWork(PlatformSearcher<? extends VideoEntity> platformSearcher) {
        Assert.notNull(platformSearcher, "platformSearcher is null");
        platformSearcherMap.put(platformSearcher.getPlatform(), platformSearcher);
    }

    public void setCompletionService(Executor executor) {
        this.completionService = new ExecutorCompletionService<>(executor);
    }


}
