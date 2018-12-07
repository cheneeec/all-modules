package com.earnest.web.autoconfigure;

import com.earnest.crawler.proxy.HttpProxySupplier;
import com.earnest.video.episode.EpisodeFetcher;
import com.earnest.video.episode.EpisodeFetcherManager;
import com.earnest.video.episode.IQiYiEpisodeFetcher;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Configuration
public class EpisodeFetcherAutoConfiguration {

    @Bean
    public EpisodeFetcher iQiYiEpisodeFetcher(
            CloseableHttpClient httpClient, ResponseHandler<String> stringResponseHandler,
            @Autowired(required = false) HttpProxySupplier httpProxySupplier) {
        IQiYiEpisodeFetcher iQiYiEpisodeFetcher = new IQiYiEpisodeFetcher(httpClient, stringResponseHandler);
        iQiYiEpisodeFetcher.setHttpProxySupplier(httpProxySupplier);
        return iQiYiEpisodeFetcher;

    }

    @Bean
    public EpisodeFetcher episodeFetcher(@Autowired(required = false) List<EpisodeFetcher> episodeFetchers) {
        EpisodeFetcherManager episodeFetcherManager = new EpisodeFetcherManager();

        //add other
        Optional.ofNullable(episodeFetchers).stream()
                .flatMap(Collection::stream).forEach(episodeFetcherManager::addWork);

        return episodeFetcherManager;
    }
}
