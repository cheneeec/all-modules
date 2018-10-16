package com.earnest.crawler.downloader;

import com.earnest.crawler.StringResponseResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Getter
public class HttpClientDownloader implements Downloader {

    private final CloseableHttpClient httpClient;

    private final HttpClientContext httpContext;


    public HttpClientDownloader(CloseableHttpClient httpClient, HttpClientContext httpContext) {
        this.httpClient = Optional.ofNullable(httpClient).orElse(HttpClients.createMinimal());
        this.httpContext = httpContext;
    }

    public HttpClientDownloader(CloseableHttpClient httpClient) {
        this(httpClient, null);
    }


    @Override
    public StringResponseResult download(HttpUriRequest request) {
        Assert.notNull(request, "request is null");
        //下载地址
        String uri = request.getRequestLine().getUri();

        log.trace("Start downloading {}", uri);
        if (httpContext == null) {
            log.debug("httpContext is null and session will not be saved");
        }
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(request, httpContext);
            log.debug("download successful,url={}", uri);
            return successAdapt(httpResponse, request);
        } catch (IOException e) {
            log.error("url:{} download failed,error:{}", uri, e.getMessage());
            return failureAdapt(request, e);
        } finally {
            HttpClientUtils.closeQuietly(httpResponse);
        }

    }

    private static StringResponseResult failureAdapt(HttpUriRequest request, IOException e) {
        StringResponseResult responseResult = new StringResponseResult();
        responseResult.setHttpRequest(request);
        responseResult.setReason(e.getMessage());
        responseResult.setSuccess(false);
        return responseResult;
    }

    @Override
    public void close() throws IOException {

        httpClient.close();

    }

    private StringResponseResult successAdapt(HttpResponse response, HttpUriRequest httpUriRequest) throws IOException {

        StringResponseResult responseResult = new StringResponseResult();

        //set httpUriRequest
        responseResult.setHttpRequest(httpUriRequest);

        Map<String, String> headers = new LinkedHashMap<>();

        Arrays.stream(response.getAllHeaders())
                .forEach(header -> headers.put(header.getName(), header.getValue()));

        responseResult.setHeaders(headers);

        responseResult.setStatus(response.getStatusLine().getStatusCode());

        String reasonPhrase = response.getStatusLine().getReasonPhrase();

        responseResult.setSuccess("OK".equalsIgnoreCase(reasonPhrase));
        responseResult.setReason(reasonPhrase);


        HttpEntity entity = response.getEntity();
        Header contentType = entity.getContentType();

        if (contentType != null) {
            // set charset
            Arrays.stream(contentType.getElements())
                    .map(e -> e.getParameterByName("charset"))
                    .filter(Objects::nonNull)
                    .findAny()
                    .map(NameValuePair::getValue).ifPresent(responseResult::setCharset);
        }


        //set entity
        responseResult.setContent(EntityUtils.toString(entity));


        //set cookies
        if (httpContext != null) {
            responseResult.setCookies(
                    httpContext.getCookieStore().getCookies().stream()
                            .collect(toMap(Cookie::getName, Cookie::getValue))
            );
        }


        return responseResult;
    }
}
