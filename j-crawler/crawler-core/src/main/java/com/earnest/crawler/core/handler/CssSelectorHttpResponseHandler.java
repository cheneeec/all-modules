package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.response.PageResponse;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.util.Set;
import java.util.function.Function;

@AllArgsConstructor
public class CssSelectorHttpResponseHandler extends AbstractHttpResponseHandler {

    private final Function<Document, Set<String>> newHttpRequestExtractor;

    @Override
    protected Set<String> extract(PageResponse pageResponse) {

        return newHttpRequestExtractor.apply(
                Jsoup.parse(pageResponse.getContent())
        );


    }
}
