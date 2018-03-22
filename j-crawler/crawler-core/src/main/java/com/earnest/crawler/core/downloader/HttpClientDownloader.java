package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.request.HttpUriRequestAdapter;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
@AllArgsConstructor

public class HttpClientDownloader implements Downloader, DownloadListener {
    private final CloseableHttpClient httpClient;
    @Getter
    private final Set<DownloadListener> downloadListeners = new HashSet<>(5);


    public HttpClientDownloader() {
        this(HttpClients.createDefault());
    }

    @Override
    public HttpResponse download(HttpRequest request) {
        log.info("Start downloading {}", request.getUrl());
        HttpUriRequestAdapter httpUriRequest = new HttpUriRequestAdapter(request);
        try {
            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpUriRequest, httpUriRequest.obtainHttpContext());
            HttpEntity httpEntity = closeableHttpResponse.getEntity();

            HttpResponse httpResponse = new HttpResponse(EntityUtils.toString(httpEntity, Consts.UTF_8));
            httpResponse.setStatus(closeableHttpResponse.getStatusLine().getStatusCode());
            httpResponse.setHttpRequest(request);

            if (Objects.nonNull(httpEntity.getContentType())) {
                httpResponse.setContentType(httpEntity.getContentType().getValue());
            }
            //关闭响应
            closeableHttpResponse.close();
            onSuccess(httpResponse);
            return httpResponse;
        } catch (IOException e) {
            onError(request, e);
            log.error("An error occurred while downloading {} ,error:{}", request.getUrl(), e.getMessage());
        }

        return null;
    }


    @Override
    public void onSuccess(HttpResponse httpResponse) {
        if (!downloadListeners.isEmpty()) {

            downloadListeners.forEach(s -> s.onSuccess(httpResponse));
        }
    }

    @Override
    public void onError(HttpRequest httpRequest, Exception e) {
        if (!downloadListeners.isEmpty()) {

            downloadListeners.forEach(s -> s.onError(httpRequest, e));
        }
    }


    @Override
    public void close() {
        try {
            if (Objects.nonNull(httpClient)) {
                httpClient.close();
            }
        } catch (IOException e) {
            log.error("An error occurred while closing the client,error:{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HttpClientDownloader addDownloadListener(DownloadListener downloadListener) {
        downloadListeners.add(downloadListener);
        return this;
    }


}
