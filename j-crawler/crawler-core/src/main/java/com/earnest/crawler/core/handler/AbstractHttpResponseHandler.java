package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
public abstract class AbstractHttpResponseHandler implements HttpResponseHandler {

    @Override
    public Set<HttpRequest> handle(HttpResponse rawResponse) {
        HttpRequest httpRequest = rawResponse.getHttpRequest();
        filter(rawResponse, httpRequest);

        return extract(rawResponse).stream().map(url -> {
            HttpRequest cloneHttpRequest = ObjectUtils.cloneIfPossible(httpRequest);
            cloneHttpRequest.setUrl(url);
            return cloneHttpRequest;

        }).collect(Collectors.toSet());
    }


    protected abstract Set<String> extract(HttpResponse httpResponse);

    private void filter(HttpResponse rawResponse, HttpRequest httpRequest) {
        String content = rawResponse.getContent();

        if (nonNull(httpRequest)) {
            //过滤
            if (httpRequest.isIgnoreHTMLHead()) {
                content = content.replaceAll("<head[^>]*?>[\\s\\S]*?</head>", "");
            }
            //去掉注释
            content = content.replaceAll("<!--[\\w\\W\\r\\n]*?-->", "");
            if (httpRequest.isIgnoreJavascript()) {
                content = content.replaceAll("<script\\b[^<]*(?:(?!<\\/script>)<[^<]*)*<\\/script>", "");
            }
            //去掉CSS
            if (httpRequest.isIgnoreCss()) {
                content = content.replaceAll("<style[^>]*?>[\\s\\S]*?</style>", "");
            }
        }

        rawResponse.setContent(content);
    }
}
