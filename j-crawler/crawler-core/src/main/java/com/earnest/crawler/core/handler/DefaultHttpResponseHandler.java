package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;


import java.util.List;
import java.util.regex.Pattern;

public class DefaultHttpResponseHandler implements HttpResponseHandler {

    private final Pattern scriptPattern = Pattern.compile("<script[^>]*?>.*?</script>");

    @Override
    public List<HttpRequest> handle(HttpResponse rawResponse) {

        HttpRequest httpRequest = rawResponse.getHttpRequest();
        filter(rawResponse, httpRequest);


        return null;
    }

    private void filter(HttpResponse rawResponse, HttpRequest httpRequest) {
        String content = rawResponse.getContent();
        //过滤
        if (httpRequest.ignoreHTMLHead()) {
            content = content.replaceAll("<head[^>]*?>.*?</head>", "");
        }
        //去掉注释
        content = content.replaceAll("<!--[/!]*?[^<>]*?>", "");
        if (httpRequest.ignoreJavascript()) {
            content = content.replaceAll("<script[^>]*?>.*?</script>", "");
        }
        //去掉CSS
        if (httpRequest.ignoreCss()) {
            content = content.replaceAll("<style[^>]*?>.*?</style>", "");
        }
        rawResponse.setContent(content);
    }
}
