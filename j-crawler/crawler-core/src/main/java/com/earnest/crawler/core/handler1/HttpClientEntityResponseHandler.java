package com.earnest.crawler.core.handler1;

import com.earnest.crawler.core.HttpClientResponseResult;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler1.Scheduler;
import lombok.AllArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @param <R> 可以获得的结果类型。四种选择：{@link String},{@link java.io.InputStream},{@link org.jsoup.nodes.Document},{@link Array}
 * @param <T>
 */
@AllArgsConstructor
public class HttpClientEntityResponseHandler<R, T> implements ResponseHandler<T> {

    private final Scheduler scheduler;

    private final Pipeline<R, T> pipeline;

    private final HttpRequestExtractor httpRequestExtractor;


    @Override
    public T handleResponse(HttpResponse response) throws IOException {

        HttpClientResponseResult<String> responseResult = createHttpClientResponseResult(response);

        //put new httpUriRequests
        Set<HttpUriRequest> newHttpUriRequests = httpRequestExtractor.extract(responseResult);

        newHttpUriRequests.forEach(scheduler::put);


        //super method
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        return entity == null ? null : handleResponseResult(responseResult);
        //super method
    }

    private T handleResponseResult(HttpClientResponseResult<String> responseResult) {
        return null;
    }

    /**
     * 根据{@link HttpResponse}创建{@link HttpClientResponseResult}
     *
     * @param response {@link HttpResponse}
     * @return {@link HttpClientResponseResult}
     * @throws IOException
     */
    private HttpClientResponseResult<String> createHttpClientResponseResult(HttpResponse response) throws IOException {
        HttpClientResponseResult<String> responseResult = new HttpClientResponseResult<>();
        Map<String, String> headers = new LinkedHashMap<>();

        HttpEntity entity = response.getEntity();

        Arrays.stream(response.getAllHeaders())
                .forEach(header -> headers.put(header.getName(), header.getValue()));

        responseResult.setHeaders(headers);

        responseResult.setStatus(response.getStatusLine().getStatusCode());

        responseResult.setSuccess("OK".equalsIgnoreCase(response.getStatusLine().getReasonPhrase()));

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
        return responseResult;
    }


    public static void main(String[] args) {
        Pipeline<String, List<HttpResponse>> pipeline = result -> Collections.emptyList();
        Class<? extends Pipeline> aClass = pipeline.getClass();
        System.out.println((aClass.getSigners()));
    }


}
