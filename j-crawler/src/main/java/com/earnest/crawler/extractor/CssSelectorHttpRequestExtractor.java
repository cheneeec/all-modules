package com.earnest.crawler.extractor;

import com.earnest.crawler.HttpResponseResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.function.Function;


public class CssSelectorHttpRequestExtractor extends AbstractHttpRequestExtractor {

    private final Function<Document, Set<String>> selectorExtractor;

    public CssSelectorHttpRequestExtractor(Function<Document, Set<String>> selectorExtractor) {
        Assert.notNull(selectorExtractor, "selectorExtractor is null");
        this.selectorExtractor = selectorExtractor;
    }


    @Override
    protected Set<String> extractUrl(HttpResponseResult<String> responseResult) {
        String baseUri = "";
        if (responseResult.getHttpRequest() != null) {
            baseUri = responseResult.getHttpRequest().getURI().toString();
        }

        return selectorExtractor.apply(
                Jsoup.parse(responseResult.getContent(),
                        baseUri
                )
        );
    }


}
