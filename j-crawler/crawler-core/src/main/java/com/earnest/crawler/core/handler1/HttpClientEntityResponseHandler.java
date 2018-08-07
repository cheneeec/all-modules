package com.earnest.crawler.core.handler1;

import com.earnest.crawler.core.HttpClientResponseResult;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.pipeline.Pipeline;
import com.earnest.crawler.core.scheduler1.Scheduler;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

@Setter
public class HttpClientEntityResponseHandler<R> implements ResponseHandler<R>, HttpResponseHandler<Document, R> {

    private Scheduler scheduler;

    private final Pipeline<Document, R> pipeline;

    private HttpRequestExtractor httpRequestExtractor;


    public HttpClientEntityResponseHandler(Pipeline<Document, R> pipeline) {
        this(null, pipeline, null);
    }

    public HttpClientEntityResponseHandler(Scheduler scheduler, Pipeline<Document, R> pipeline, HttpRequestExtractor httpRequestExtractor) {
        this.scheduler = scheduler;
        this.pipeline = pipeline;
        this.httpRequestExtractor = httpRequestExtractor;
    }

    @Override
    public R handleResponse(HttpResponse response) throws IOException {
        //super method
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {

            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }

        String content = EntityUtils.toString(response.getEntity());


        HttpClientResponseResult<String> stringHttpResponseResult = createHttpClientResponseResult(response);
        stringHttpResponseResult.setContent(content);

        //
        extractNewHttpRequests(stringHttpResponseResult);

        HttpClientResponseResult<Document> documentResponseResult = createHttpClientResponseResult(response);
        documentResponseResult.setContent(Jsoup.parse(content));

        return entity == null ? null : handle(documentResponseResult);

    }

    /**
     * 从结果中提取新的{@link HttpUriRequest}。
     *
     * @param responseResult
     */
    private void extractNewHttpRequests(HttpResponseResult<String> responseResult) {
        if (ObjectUtils.allNotNull(httpRequestExtractor, scheduler)) {
            //create responseResult

            //extract new httpUriRequests
            Set<HttpUriRequest> newHttpUriRequests = httpRequestExtractor.extract(responseResult);

            //add new httpUriRequests to scheduler
            newHttpUriRequests.forEach(scheduler::put);
        }
    }


    /**
     * 根据{@link HttpResponse}创建{@link HttpClientResponseResult}
     *
     * @param response {@link HttpResponse}
     * @return {@link HttpClientResponseResult}
     * @throws IOException
     */
    private <Q> HttpClientResponseResult<Q> createHttpClientResponseResult(HttpResponse response) throws IOException {

        HttpClientResponseResult<Q> responseResult = new HttpClientResponseResult<>();

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
//        responseResult.setContent(EntityUtils.toString(entity));


        //set httpUriRequest
        HttpUriRequest httpUriRequest = HttpClientResponseContextHolder.getHttpUriRequest(response);

        responseResult.setHttpRequest(httpUriRequest);

        return responseResult;
    }


    @Override
    public R handle(HttpResponseResult<Document> responseResult) {
        return pipeline.pipe(responseResult);
    }
}
