package com.earnest.video.search;

import com.alibaba.fastjson.util.IOUtils;
import com.earnest.crawler.proxy.HttpProxySupplier;
import com.earnest.video.entity.Platform;
import com.earnest.video.entity.Video;
import com.earnest.video.exception.UnknownException;
import com.earnest.video.exception.UnsupportedPlatformException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "search")
public class DefaultPlatformSearcherManager implements PlatformSearcherManager {

    private final Map<Platform, PlatformSearcher<? extends Video>> platformSearcherMap = new LinkedHashMap<>(5);

    @Getter
    private CompletionService<Page<? extends Video>> completionService;

    @Getter
    @Setter
    private Integer ignoreSecondTimeOut = 10; //忽略多少秒后的结果


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
    @Cacheable(key = "#keyword+'['+#pageRequest.getPageNumber()+','+#pageRequest.getPageSize()+']'")
    public Page<Video> search(String keyword, Pageable pageRequest) {


        List<? extends Page<? extends Video>> results = platformSearcherMap.values()
                .stream()
                .map(search -> (Callable<Page<? extends Video>>) () -> search.search(keyword, pageRequest))
                .map(completionService::submit) //执行
                .parallel()
                .map(getResult())//取结果
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //收集
        long totalElements = 0L;

        List<Video> content = new ArrayList<>(pageRequest.getPageSize() * results.size());


        for (Page<? extends Video> baseVideoEntities : results) {
            totalElements += baseVideoEntities.getTotalElements();
            content.addAll(baseVideoEntities.getContent());
        }


        return new PageImpl<>(content, pageRequest, totalElements);
    }


    @Override
    public Platform getPlatform() {
        return null;
    }

    private Function<Future<Page<? extends Video>>, ? extends Page<? extends Video>> getResult() {
        return future -> {
            try {
                return future.get(ignoreSecondTimeOut, TimeUnit.SECONDS);
            } catch (Exception e) { //发生错误时忽略
                e.printStackTrace();
                if (log.isDebugEnabled() && e instanceof TimeoutException) {
                    log.debug("A task timed out has been ignored,error:{}", e.getMessage());
                }
                future.cancel(true);
                System.err.println(e.getMessage());
                throw new UnknownException(e.getMessage(),e);
            }
        };
    }


    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(key = "#keyword+'['+#pageRequest.getPageNumber()+','+#pageRequest.getPageSize()+']:'+#platform")
    public Page<Video> search(String keyword, Pageable pageRequest, Platform platform) throws IOException {

        if (platform == null) {
            return search(keyword, pageRequest);
        }

        PlatformSearcher<? extends Video> platformSearcher = platformSearcherMap.get(platform);

        if (platformSearcher != null) {
            return (Page<Video>) platformSearcher.search(keyword, pageRequest);
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
    public void setHttpProxySupplier(HttpProxySupplier httpProxySupplier) {
        platformSearcherMap.values().forEach(s -> s.setHttpProxySupplier(httpProxySupplier));
    }


    @Override
    public void addWork(PlatformSearcher<? extends Video> platformSearcher) {
        Assert.notNull(platformSearcher, "platformSearcher is null");
        platformSearcherMap.put(platformSearcher.getPlatform(), platformSearcher);
    }

    public void setCompletionService(Executor executor) {
        this.completionService = new ExecutorCompletionService<>(executor);
    }


}
