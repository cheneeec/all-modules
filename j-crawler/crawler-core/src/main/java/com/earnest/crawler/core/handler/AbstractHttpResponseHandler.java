package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.AbstractHttpRequest;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractHttpResponseHandler implements HttpResponseHandler {

    @Override
    public Set<HttpRequest> handle(HttpResponse rawResponse) {
        HttpRequest httpRequest = rawResponse.getHttpRequest();
        filter(rawResponse, httpRequest);
        Set<String> newUrls = extract(rawResponse);

        return newUrls.stream().map(url -> {
            HttpRequest cloneHttpRequest = ObjectUtils.cloneIfPossible(httpRequest);
            if (cloneHttpRequest instanceof AbstractHttpRequest) {
                AbstractHttpRequest abstractHttpRequest = (AbstractHttpRequest) cloneHttpRequest;
                abstractHttpRequest.setUrl(url);
                return abstractHttpRequest;
            } else
                throw new IllegalStateException("Cannot reset new URL for " + httpRequest.getClass());

        }).collect(Collectors.toSet());
    }


    protected abstract Set<String> extract(HttpResponse httpResponse);

    private void filter(HttpResponse rawResponse, HttpRequest httpRequest) {
        String content = rawResponse.getContent();
        //过滤
        if (httpRequest.ignoreHTMLHead()) {
            content = content.replaceAll("<head[^>]*?>[\\s\\S]*?</head>", "");
        }
        //去掉注释
        content = content.replaceAll("<!--[\\w\\W\\r\\n]*?-->", "");
        if (httpRequest.ignoreJavascript()) {
            content = content.replaceAll("<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");
        }
        //去掉CSS
        if (httpRequest.ignoreCss()) {
            content = content.replaceAll("<style[^>]*?>[\\s\\S]*?</style>", "");
        }
        rawResponse.setContent(content);
    }
}
