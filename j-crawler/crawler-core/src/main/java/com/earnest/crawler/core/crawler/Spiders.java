package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.handler.CssSelectorHttpResponseHandler;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.handler.RegexHttpResponseHandler;
import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Slf4j
public class Spiders<T> {

  /*  private Crawler<T> crawler;
    private int threadNumber;
    private String urlRegex;
    private Function<Document, Set<String>> cssSelector;
    private String[] jsonConfigFilePath;



    public Spiders loadConfig() {
        return null;
    }

    public Spiders loadConfig(String... jsonConfigFilePath) {
        return null;
    }

    public Spiders start() {
        return null;
    }

    public Spiders thread(int threadNumber) {
        this.threadNumber = threadNumber;
        return this;
    }

    public Spiders from(String url) {
        return from(new HttpGetRequest(url));
    }

    public Spiders from(HttpRequest httpRequest) {

        return this;
    }

    public Spiders match(String urlRegex) {
        this.urlRegex = urlRegex;
        return cssSelector(null);
    }

    public Spiders cssSelector(Function<Document, Set<String>> cssSelector) {
        this.cssSelector = cssSelector;
        return match(null);
    }

    private void setHttpResponseHandlerForCrawler() {
        if (StringUtils.isNotBlank(urlRegex)) {
            crawler.setHttpResponseHandler(new RegexHttpResponseHandler(urlRegex));
            return;
        }
        if (Objects.nonNull(cssSelector)) {
            crawler.setHttpResponseHandler(new CssSelectorHttpResponseHandler(cssSelector));
            return;
        }
        //只会爬取一页
        crawler.setHttpResponseHandler(httpResponse -> Collections.emptySet());
        log.warn("Since {} is not set,Will not get a new link", HttpResponseHandler.class);

    }*/
}
