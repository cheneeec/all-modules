package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.extractor.CssSelectorHttpRequestExtractor;
import com.earnest.crawler.core.extractor.EmptyHttpRequestExtractor;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.extractor.RegexHttpRequestExtractor;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.function.Function;

public class HttpUriRequestExtractorConfigurer extends AbstractSpiderConfigurer<HttpRequestExtractor> {


    private HttpRequestExtractor requestExtractor;

    public HttpUriRequestExtractorConfigurer(SpiderBuilder builder) {
        super(builder);
    }

    /**
     * 使用正则表达式提取新的链接。
     *
     * @param pattern
     * @return
     */
    public HttpUriRequestExtractorConfigurer match(String pattern) {
        this.requestExtractor = new RegexHttpRequestExtractor(pattern);
        return this;
    }

    /**
     * 使用css选择器提取新的链接。
     *
     * @param cssSelectorExtractor
     * @return
     */
    public HttpUriRequestExtractorConfigurer select(Function<Document, Set<String>> cssSelectorExtractor) {
        this.requestExtractor = new CssSelectorHttpRequestExtractor(cssSelectorExtractor);
        return this;
    }

    /**
     * 选取一个固定的范围获得链接。
     *
     * @return
     */
    public HttpUriRequestExtractorConfigurer range() {
        this.requestExtractor = new EmptyHttpRequestExtractor();
        return this;
    }


    @Override
    HttpRequestExtractor build() {
        return requestExtractor;
    }


}
