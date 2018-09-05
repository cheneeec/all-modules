package com.earnest.video.configuration;

import com.earnest.crawler.core.Browser;
import com.earnest.crawler.core.proxy.HttpProxyPool;
import com.earnest.crawler.core.proxy.SimpleHttpProxyProvider;
import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.episode.EpisodeFetcher;
import com.earnest.video.episode.EpisodeFetcherManager;
import com.earnest.video.search.IQiYiPlatformHttpClientSearcher;
import com.earnest.video.search.PlatformSearcher;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                .setMaxConnTotal(8)
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

    /**
     * @return {@link HttpProxyPool}
     */
    @Bean
    public HttpProxyPool httpProxyPool() {
        SimpleHttpProxyProvider httpProxyProvider = SimpleHttpProxyProvider.INSTANCE;
        httpProxyProvider.initializeHttpProxyPool();
        return httpProxyProvider;
    }

    //==========================Manager==================

    @Bean
    public EpisodeFetcher episodeFetcher() {
        return new EpisodeFetcherManager(httpClient(), responseHandler());
    }

    @Bean
    public PlatformSearcher<? extends BaseVideoEntity> platformSearcher() {
        return new IQiYiPlatformHttpClientSearcher(httpClient(), responseHandler(), httpProxyPool());
    }
    //==========================//Manager==================
}
