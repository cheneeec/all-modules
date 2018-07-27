package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.event.DownloadErrorEvent;
import com.earnest.crawler.core.event.DownloadSuccessEvent;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.request.HttpUriRequestAdapter;
import com.earnest.crawler.core.response.PageResponse;
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
public class HttpClientDownloader extends AbstractDownloader implements MultiThreadBean {

    private final HttpClient httpClient;

    public HttpClientDownloader() {
        this(HttpClients.createSystem());
    }


    @Override
    public PageResponse download(HttpRequest request) {

        log.info("Start downloading {}", request.getUrl());
        HttpUriRequestAdapter httpUriRequest = new HttpUriRequestAdapter(request);
        try {
            org.apache.http.HttpResponse response = httpClient.execute(httpUriRequest, httpUriRequest.obtainHttpContext());
            HttpEntity httpEntity = response.getEntity();

            PageResponse pageResponse = new PageResponse(EntityUtils.toString(httpEntity, Consts.UTF_8),request);
            pageResponse.setStatus(response.getStatusLine().getStatusCode());


            if (nonNull(httpEntity.getContentType())) {
                pageResponse.setContentType(httpEntity.getContentType().getValue());
            }
            //关闭响应
            if (response instanceof Closeable) {
                ((Closeable) response).close();
            }
            onSuccess(new DownloadSuccessEvent(pageResponse));
            return pageResponse;
        } catch (IOException e) {
            onError(new DownloadErrorEvent(request, e));
            log.error("An error occurred while downloading {} ,error:{}", request.getUrl(), e.getMessage());
            PageResponse pageResponse = new PageResponse(e.getMessage());
            pageResponse.setStatus(500);
            return pageResponse;
        }


    }


    @Override
    public void close() {
        if (nonNull(httpClient) && httpClient instanceof Closeable) {
            try {
                ((Closeable) httpClient).close();
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

    @Override
    public void setThread(int num) {
        //设置最大连接数，系统默认是5x2
        int maxConnectionCount = (int) Math.ceil(((double) num / 2));

        System.getProperties().setProperty("http.maxConnections", String.valueOf(maxConnectionCount));
        log.info("set SystemProperty: [http.maxConnections={}]", maxConnectionCount * 2);
    }


}
