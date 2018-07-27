package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.HtmlResponsePage;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;



@Slf4j
public class HttpClientDownloader implements Downloader<HttpRequest, HtmlResponsePage> {

    private final HttpClient httpClient;

    public HttpClientDownloader(HttpClient httpClient) {
        this.httpClient = httpClient;
    }


    @Override
    public HtmlResponsePage download(HttpRequest request) throws IOException {

        Assert.notNull(request, "request is null");

        log.info("Start downloading {}", request.getUrl());

        //build a basic request
        RequestBuilder requestBuilder = RequestBuilder.create(request.getMethod())
                .setCharset(Charset.forName(request.getCharset()))
                .setUri(request.getUrl());

        //add headers
        if (!CollectionUtils.isEmpty(request.getHeaders()))
            request.getHeaders().forEach(requestBuilder::addHeader);

        //add parameters
        if (!CollectionUtils.isEmpty(request.getParameters()))
            request.getParameters().forEach(requestBuilder::addParameter);

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

        HttpResponse response = httpClient.execute(requestBuilder.build(), context);

        //consume entity
        HttpEntity entity = response.getEntity();
        EntityUtils.consumeQuietly(entity);


        HtmlResponsePage htmlResponsePage = new HtmlResponsePage(request, entity, response.getStatusLine(), response.getLocale(), null, response.getAllHeaders());

        //关闭响应
        if (response instanceof Closeable) {
            ((Closeable) response).close();
        }

        return htmlResponsePage;

    }

    @Override
    public void close() throws IOException {
        if (httpClient instanceof Closeable) {
            ((Closeable) httpClient).close();
        }
    }
}
