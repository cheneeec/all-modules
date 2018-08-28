package com.earnest.video.episode;

import com.earnest.crawler.core.Browser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CloseableHttpClientFactoryBean implements FactoryBean<CloseableHttpClient>, DisposableBean {

    private final CloseableHttpClient httpClient = HttpClients.custom()
            .setUserAgent(Browser.GOOGLE.userAgent())
            .setMaxConnTotal(8)
            .build();

    public static final CloseableHttpClientFactoryBean INSTANCE = new CloseableHttpClientFactoryBean();


    @Override
    public CloseableHttpClient getObject() {
        return httpClient;
    }

    @Override
    public Class<?> getObjectType() {
        return httpClient.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void destroy() throws Exception {
        httpClient.close();
    }
}
