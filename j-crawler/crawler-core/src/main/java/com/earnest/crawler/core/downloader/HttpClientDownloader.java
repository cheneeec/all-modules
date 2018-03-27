package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.request.HttpUriRequestAdapter;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;


import static java.util.Objects.nonNull;

@Slf4j
@AllArgsConstructor
public class HttpClientDownloader extends AbstractDownloader {

    private final HttpClient httpClient;

    public HttpClientDownloader() {
        this(HttpClients.createSystem());
    }


    @Override
    public HttpResponse download(HttpRequest request) {
        log.info("Start downloading {}", request.getUrl());
        HttpUriRequestAdapter httpUriRequest = new HttpUriRequestAdapter(request);
        try {
            org.apache.http.HttpResponse response = httpClient.execute(httpUriRequest, httpUriRequest.obtainHttpContext());
            HttpEntity httpEntity = response.getEntity();

            HttpResponse httpResponse = new HttpResponse(EntityUtils.toString(httpEntity, Consts.UTF_8));
            httpResponse.setStatus(response.getStatusLine().getStatusCode());
            httpResponse.setHttpRequest(request);

            if (nonNull(httpEntity.getContentType())) {
                httpResponse.setContentType(httpEntity.getContentType().getValue());
            }
            //关闭响应
            if (response instanceof Closeable) {
                ((Closeable) response).close();
            }
            onSuccess(httpResponse);
            return httpResponse;
        } catch (IOException e) {
            onError(request, e);
            log.error("An error occurred while downloading {} ,error:{}", request.getUrl(), e.getMessage());
        }

        return null;
    }


    @Override
    public void close() {
        try {
            if (nonNull(httpClient) && httpClient instanceof Closeable) {
                ((Closeable) httpClient).close();
            }
        } catch (IOException e) {
            log.error("An error occurred while closing the client,error:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                ((Closeable) httpClient).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
