package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.downloader1.Downloader;
import com.earnest.crawler.core.downloader1.HttpClientDownloader;
import com.earnest.crawler.core.request.HttpProxy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.util.Assert;

import java.util.*;


public class DownloaderConfigurer extends RequestConfigConfigurer<Downloader> {

    private final HttpClientBuilder httpClientBuilder = HttpClients.custom();


    public DownloaderConfigurer setProxy(HttpProxy httpProxy) {
        Assert.notNull(httpProxy, "httpProxy is null");
        httpClientBuilder.setProxy(httpProxy.getHttpHost());
        return this;
    }

    public DownloaderConfigurer setThreadNumber(int thread) {

        sharedObjectMap.put(Integer.class, Collections.singletonList(thread));

        httpClientBuilder.setMaxConnTotal(this.thread);
        return this;
    }

    public int getThread() {
        return thread;
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
    public void init() {

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
    public void configure() {

        //移除上下文的cookieStore
        Object o = sharedObjectMap.get(CookieStore.class).remove(1);

        Assert.state(o != null, "session cookieStore is null");


        //获得HttpClientContext
        HttpClientContext httpClientContext = (HttpClientContext) sharedObjectMap.remove(HttpClientContext.class).get(0);

        sharedObjectMap.put(Downloader.class,
                Collections.singletonList(
                        new HttpClientDownloader(httpClientBuilder.build(), httpClientContext)
                ));

    }


}
