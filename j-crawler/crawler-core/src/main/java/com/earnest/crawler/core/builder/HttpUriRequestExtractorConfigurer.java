package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.extractor.CssSelectorHttpRequestExtractor;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.extractor.RegexHttpRequestExtractor;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class HttpUriRequestExtractorConfigurer extends AbstractSpiderConfigurer<HttpRequestExtractor> {


    private HttpRequestExtractor requestExtractor;

    public HttpUriRequestExtractorConfigurer(SpiderBuilder builder) {
        super(builder);
    }


    public HttpUriRequestExtractorConfigurer match(String pattern) {
        this.requestExtractor = new RegexHttpRequestExtractor(pattern);
        return this;
    }

    public HttpUriRequestExtractorConfigurer select(Function<Document, Set<String>> cssSelectorExtractor) {
        this.requestExtractor = new CssSelectorHttpRequestExtractor(cssSelectorExtractor);
        return this;
    }




    @Override
    HttpRequestExtractor build() {
        return requestExtractor;
    }


}
