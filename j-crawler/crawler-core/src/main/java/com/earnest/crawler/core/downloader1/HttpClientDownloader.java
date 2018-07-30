package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.HtmlResponsePage;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;


@Slf4j
public class HttpClientDownloader implements Downloader {

    private CloseableHttpClient httpClient;

    public HttpClientDownloader() {
        this.httpClient = initializeHttpClient();
    }

    @Override
    public HttpResponse download(HttpRequest request) throws IOException {

        Assert.notNull(request, "request is null");

        log.info("start downloading {}", request.getUrl());

        //create a new cookieStore(thread-safe)
        BasicCookieStore cookieStore = new BasicCookieStore();

        if (!CollectionUtils.isEmpty(request.getCookies()))
            request.getCookies().forEach((k, v) -> cookieStore.addCookie(new BasicClientCookie(k, v)));

        HttpClientContext context = HttpClientContext.create();

        context.setCookieStore(cookieStore);

        context.setRequestConfig(
                RequestConfig.custom()
                        .setConnectTimeout(request.getConnectTimeout())
                        .build()
        );

        CloseableHttpResponse response = httpClient.execute(HttpUriRequestConverter.convert(request), context);

        //consume entity
        HttpEntity entity = response.getEntity();
        EntityUtils.consumeQuietly(entity);

        //关闭响应
        response.close();

        return response;
    }

    /**
     * 获取会话的上下文。
     *
     * @return {@link HttpContext}
     */
    protected HttpContext obtainHttpContext() {
        return null;
    }

    protected CloseableHttpClient initializeHttpClient() {
        System.out.println("HttpClientDownloader");
        return HttpClients.createDefault();
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }


}
