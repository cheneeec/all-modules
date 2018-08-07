package com.earnest.crawler.core.handler2;

import com.earnest.crawler.core.HttpClientResponseResult;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.handler1.HttpClientResponseContextHolder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractHttpClientResponseHandler<T> implements ResponseHandler<HttpResponseResult<T>> {

    @Override
    public HttpResponseResult<T> handleResponse(HttpResponse response) throws IOException {

        HttpClientResponseResult<T> responseResult = new HttpClientResponseResult<>();

        Map<String, String> headers = new LinkedHashMap<>();

        Arrays.stream(response.getAllHeaders())
                .forEach(header -> headers.put(header.getName(), header.getValue()));

        responseResult.setHeaders(headers);

        responseResult.setStatus(response.getStatusLine().getStatusCode());

        responseResult.setSuccess("OK".equalsIgnoreCase(response.getStatusLine().getReasonPhrase()));

        HttpEntity entity = response.getEntity();

        // set charset
        Arrays.stream(entity.getContentType().getElements())
                .map(e -> e.getParameterByName("charset"))
                .findAny()
                .map(NameValuePair::getValue).ifPresent(responseResult::setCharset);

        //set entity

        responseResult.setContent(getContent(response));


        //set httpUriRequest
        HttpUriRequest httpUriRequest = HttpClientResponseContextHolder.getHttpUriRequest(response);

        responseResult.setHttpRequest(httpUriRequest);

        if (!responseResult.isSuccess()) {
            EntityUtils.consume(response.getEntity());
        }
        if (response instanceof CloseableHttpResponse) {
            ((CloseableHttpResponse) response).close();
        }

        return responseResult;
    }


    protected abstract T getContent(HttpResponse response) throws IOException;


}
