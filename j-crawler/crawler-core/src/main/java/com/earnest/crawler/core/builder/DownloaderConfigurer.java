package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.downloader.HttpClientDownloader;
import com.earnest.crawler.core.proxy.HttpProxy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.util.Assert;

import java.util.*;


public class DownloaderConfigurer extends RequestConfigConfigurer {

    private final HttpClientBuilder httpClientBuilder = HttpClients.custom();


    public DownloaderConfigurer setProxy(HttpProxy httpProxy) {
        Assert.notNull(httpProxy, "httpProxy is null");
        httpClientBuilder.setProxy(httpProxy.getHttpHost());
        return this;
    }

    public DownloaderConfigurer setThreadNumber(int thread) {
        sharedObjectMap.put(Integer.class, Collections.singletonList(thread));

        httpClientBuilder.setMaxConnTotal(thread);

        return this;
    }


    public DownloaderConfigurer addCookie(String name, String value) {
        ((CookieStore) sharedObjectMap.get(CookieStore.class).get(0))
                .addCookie(new BasicClientCookie(name, value));
        return this;
    }

    public DownloaderConfigurer userAgent(String userAgent) {
        httpClientBuilder.setUserAgent(userAgent);
        return this;
    }

    public DownloaderConfigurer addCookies(Map<String, String> cookies) {
        CookieStore cookieStore = (CookieStore) sharedObjectMap.get(CookieStore.class).get(0);

        cookies.forEach((k, v) -> cookieStore.addCookie(new BasicClientCookie(k, v)));

        cookies.forEach(this::addCookie);
        return this;
    }


    @Override
    protected int order() {
        return 1;
    }

    @Override
    void init() {

        //放入全局cookieStore
        List<CookieStore> cookieStores = new ArrayList<>(2);
        cookieStores.add(new BasicCookieStore());
        sharedObjectMap.put(CookieStore.class, cookieStores);
        //放入HttpClientContext
        HttpClientContext httpClientContext = new HttpClientContext();
        httpClientContext.setCookieStore(cookieStores.get(0));
        sharedObjectMap.put(HttpClientContext.class, Collections.singletonList(httpClientContext));
        //放入thread
        sharedObjectMap.put(Integer.class, Collections.singletonList(5));


    }

    @Override
    void configure() {

        //移除session的cookieStore
        Object sessionCookieStore = sharedObjectMap.get(CookieStore.class).remove(1);

        Assert.state(sessionCookieStore != null, "session cookieStore is null");


        //获得HttpClientContext
        HttpClientContext httpClientContext = (HttpClientContext) sharedObjectMap.remove(HttpClientContext.class).get(0);

        //将session的cookieStore
        httpClientContext.setCookieStore((CookieStore) sessionCookieStore);

        //设置全局的cookieStore
        CloseableHttpClient httpClient = httpClientBuilder
                .setDefaultCookieStore(((CookieStore) sharedObjectMap.remove(CookieStore.class).get(0)))
                .build();

        sharedObjectMap.put(Downloader.class,
                Collections.singletonList(
                        new HttpClientDownloader(httpClient, httpClientContext)
                ));

    }


}
