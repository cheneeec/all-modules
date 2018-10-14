package com.earnest.video.config;

import com.earnest.crawler.core.Browser;
import com.earnest.crawler.core.proxy.HttpProxyPool;
import com.earnest.crawler.core.proxy.FixedHttpProxyProvider;
import com.earnest.video.core.episode.IQiYiEpisodeFetcher;
import com.earnest.video.core.search.DefaultPlatformSearcherManager;
import com.earnest.video.core.search.IQiYiPlatformHttpClientSearcher;
import com.earnest.video.core.episode.EpisodeFetcher;
import com.earnest.video.core.episode.EpisodeFetcherManager;
import com.earnest.video.core.search.PlatformSearcherManager;
import com.earnest.video.core.parser.StoneApiVideoAddressParser;
import com.earnest.video.core.parser.VideoAddressParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 所有的单例<code>Bean</code>的配置。都交由<code>Spring</code>容器管理。
 */
@Configuration
public class SingletonBeanConfig {

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.custom()
                .setUserAgent(Browser.GOOGLE.userAgent())
                .setMaxConnTotal(100)
                .build();
    }

    @Bean
    public ResponseHandler<String> responseHandler() {
        return new AbstractResponseHandler<String>() {
            @Override
            public String handleEntity(HttpEntity entity) throws IOException {
                return EntityUtils.toString(entity, Charset.defaultCharset());
            }
        };
    }

    @Bean
    public VideoAddressParser videoAddressParser() {
        return new StoneApiVideoAddressParser();
    }

    /**
     * @return {@link HttpProxyPool}
     */
    @Bean
    public HttpProxyPool httpProxyPool() throws Exception {
        FixedHttpProxyProvider httpProxyProvider = new FixedHttpProxyProvider();
        httpProxyProvider.initializeHttpProxyPool();
        return httpProxyProvider;
    }

    //==========================Manager==================

    @Bean
    public EpisodeFetcher episodeFetcher() throws Exception {
        EpisodeFetcherManager episodeFetcherManager = new EpisodeFetcherManager();
        episodeFetcherManager.setHttpProxyPool(httpProxyPool());
        //add iQiYi
        episodeFetcherManager.addWork(new IQiYiEpisodeFetcher(httpClient(), responseHandler()));

        return episodeFetcherManager;
    }


    @Bean
    public PlatformSearcherManager platformSearcherManager() throws Exception {
        DefaultPlatformSearcherManager platformSearcherManager = new DefaultPlatformSearcherManager();
        platformSearcherManager.setCompletionService(threadPoolTaskExecutor());
        //add IQiYi
        platformSearcherManager.addWork(new IQiYiPlatformHttpClientSearcher(httpClient(), responseHandler(), httpProxyPool()));


        return platformSearcherManager;
    }

    //==========================//Manager==================


    //=========================Spring Thread Pool=================
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);//线程池维护的最少数量
        threadPoolTaskExecutor.setMaxPoolSize(5000);//线程池维护的最大数量
        threadPoolTaskExecutor.setKeepAliveSeconds(60 * 60);
        threadPoolTaskExecutor.setQueueCapacity(500);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
    //=======================//Spring Thread Pool=================

}
