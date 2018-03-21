package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import org.jsoup.Jsoup;

import java.util.List;

public abstract class AbstractHttpResponseHandler implements HttpResponseHandler {

    @Override
    public List<HttpRequest> handle(HttpResponse rawResponse) {
        HttpRequest httpRequest = rawResponse.getHttpRequest();
        filter(rawResponse, httpRequest);
        return extract(rawResponse);
    }


    protected abstract List<HttpRequest> extract(HttpResponse httpResponse);

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
