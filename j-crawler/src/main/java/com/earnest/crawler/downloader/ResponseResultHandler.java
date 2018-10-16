package com.earnest.crawler.downloader;

import com.earnest.crawler.StringResponseResult;
import com.earnest.crawler.handler.HttpClientResponseContextHolder;
import lombok.AllArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
public class ResponseResultHandler implements ResponseHandler<StringResponseResult> {

    private final HttpClientContext httpContext;

    public ResponseResultHandler() {
        this(null);
    }

    @Override
    public StringResponseResult handleResponse(HttpResponse response) throws IOException {

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
        HttpUriRequest httpUriRequest = HttpClientResponseContextHolder.getHttpUriRequest(response);

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
