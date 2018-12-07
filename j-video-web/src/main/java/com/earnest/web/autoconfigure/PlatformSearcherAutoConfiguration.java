package com.earnest.web.autoconfigure;

import com.earnest.crawler.proxy.HttpProxySupplier;
import com.earnest.video.search.DefaultPlatformSearcherManager;
import com.earnest.video.search.IQiYiPlatformHttpClientSearcher;
import com.earnest.video.search.PlatformSearcher;
import com.earnest.video.search.PlatformSearcherManager;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Configuration
public class PlatformSearcherAutoConfiguration {

    @Bean
    public PlatformSearcher iQiYiPlatformHttpClientSearcher(
            HttpClient httpClient, ResponseHandler<String> responseHandler,
            @Autowired(required = false) HttpProxySupplier httpProxySupplier) {
        //add IQiYi
        return new IQiYiPlatformHttpClientSearcher(httpClient, responseHandler, httpProxySupplier);
    }

    @Bean
    public PlatformSearcherManager platformSearcherManager(@Autowired(required = false) List<PlatformSearcher> platformSearchers,
                                                           ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        DefaultPlatformSearcherManager platformSearcherManager = new DefaultPlatformSearcherManager();
        platformSearcherManager.setCompletionService(threadPoolTaskExecutor);


        //add other
        Optional.ofNullable(platformSearchers).stream()
                .flatMap(Collection::stream).forEach(platformSearcherManager::addWork);

        return platformSearcherManager;
    }
}
