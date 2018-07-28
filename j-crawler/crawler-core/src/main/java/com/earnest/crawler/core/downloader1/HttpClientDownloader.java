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
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;


@Slf4j
@AllArgsConstructor
public class HttpClientDownloader implements Downloader<HttpResponse> {

    private final CloseableHttpClient httpClient;


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


//        HtmlResponsePage htmlResponsePage = new HtmlResponsePage(request, entity, response.getStatusLine(), response.getLocale(), null, response.getAllHeaders());

        //关闭响应
        response.close();

        return response;
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
