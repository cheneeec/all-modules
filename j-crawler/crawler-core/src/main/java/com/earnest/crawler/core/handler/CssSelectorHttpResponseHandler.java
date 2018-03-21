package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.AbstractHttpRequest;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class CssSelectorHttpResponseHandler extends AbstractHttpResponseHandler {

    private final Function<Document, Iterable<String>> newHttpRequestExtractor;

    @Override
    protected List<HttpRequest> extract(HttpResponse httpResponse) {

        Iterable<String> newUrls = newHttpRequestExtractor.apply(
                Jsoup.parse( httpResponse.getContent()));

        HttpRequest httpRequest = httpResponse.getHttpRequest();

        List<HttpRequest> newHttpRequestsResult = new ArrayList<>();

        if (httpRequest instanceof AbstractHttpRequest) {
            AbstractHttpRequest request = (AbstractHttpRequest) httpRequest;
            //组件新的httpRequest
            newUrls.forEach(u -> {
                AbstractHttpRequest cloneRequest = request.clone();
                cloneRequest.setUrl(u);
                newHttpRequestsResult.add(cloneRequest);
            });
        } else {
            //TODO 待添加其他扩展
            throw new IllegalStateException("class: "+httpRequest.getClass()+" is not supported");
        }

        return newHttpRequestsResult;
    }
}
