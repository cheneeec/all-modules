package com.earnest.crawler.core.downloader;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.request.HttpUriRequestAdapter;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class HttpClientDownloader implements Downloader {

    private final CloseableHttpClient httpClient;

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
            return httpResponse;

        } catch (IOException e) {
            log.error("An error occurred while downloading {} ,error:{}", request.getUrl(), e.getMessage());
        }

        return null;
    }

    @Override
    public boolean shutdown() {
        if (Objects.nonNull(httpClient)) {
            try {
                httpClient.close();
                return true;
            } catch (IOException e) {
                log.error("An error occurred while closing the client,error:{}", e.getMessage());
                return false;
            }
        }
        return true;

    }


}
