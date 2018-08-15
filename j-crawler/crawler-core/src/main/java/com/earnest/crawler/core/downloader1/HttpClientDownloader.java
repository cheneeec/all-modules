package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.StringResponseResult;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Getter
public class HttpClientDownloader implements Downloader {

    private final CloseableHttpClient httpClient;

    private final HttpClientContext httpContext;
   /* @Setter
    private ResponseHandler<StringResponseResult> responseResultHandler;*/


    public HttpClientDownloader(CloseableHttpClient httpClient, HttpClientContext httpContext) {
        this.httpClient = Optional.ofNullable(httpClient).orElse(HttpClients.createMinimal());
        this.httpContext = httpContext;
//        this.responseResultHandler = new ResponseResultHandler(httpContext);
    }

    public HttpClientDownloader(CloseableHttpClient httpClient) {
        this(httpClient, null);
    }

    @Override
    public StringResponseResult download(HttpUriRequest request) {
        Assert.notNull(request, "request is null");
        log.trace("Start downloading {}", request.getURI());
        if (httpContext == null) {
            log.debug("httpContext is null and session will not be saved");
        }

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request, httpContext);
            log.debug("download successful,url={}", request.getURI().toString());
            return convert(httpResponse, request);
        } catch (IOException e) {
            log.error("url:{} download failed,error:{}", request.getURI().toString(), e.getMessage());
            StringResponseResult responseResult = new StringResponseResult();
            responseResult.setReason(e.getMessage());
            responseResult.setSuccess(false);
            return responseResult;
        }

    }

    @Override
    public void close() throws IOException {

        httpClient.close();

    }

    private StringResponseResult convert(HttpResponse response, HttpUriRequest httpUriRequest) throws IOException {

        StringResponseResult responseResult = new StringResponseResult();

        Map<String, String> headers = new LinkedHashMap<>();

        Arrays.stream(response.getAllHeaders())
                .forEach(header -> headers.put(header.getName(), header.getValue()));

        responseResult.setHeaders(headers);

        responseResult.setStatus(response.getStatusLine().getStatusCode());

        String reasonPhrase = response.getStatusLine().getReasonPhrase();

        responseResult.setSuccess("OK".equalsIgnoreCase(reasonPhrase));
        responseResult.setReason(reasonPhrase);


        HttpEntity entity = response.getEntity();

        // set charset
        Arrays.stream(entity.getContentType().getElements())
                .map(e -> e.getParameterByName("charset"))
                .findAny()
                .map(NameValuePair::getValue).ifPresent(responseResult::setCharset);

        //set entity
        responseResult.setContent(EntityUtils.toString(entity));


        //set httpUriRequest
        responseResult.setHttpRequest(httpUriRequest);

        //set cookies
        if (httpContext != null) {
            responseResult.setCookies(
                    httpContext.getCookieStore().getCookies().stream()
                            .collect(toMap(Cookie::getName, Cookie::getValue))
            );
        }


        if (!responseResult.isSuccess()) {
            EntityUtils.consume(response.getEntity());
        }

        if (response instanceof CloseableHttpResponse) {
            ((CloseableHttpResponse) response).close();
        }

        return responseResult;
    }
}
