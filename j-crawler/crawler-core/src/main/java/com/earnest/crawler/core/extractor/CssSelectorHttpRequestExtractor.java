package com.earnest.crawler.core.extractor;

import com.earnest.crawler.core.HTMLDocumentHttpResponseResult;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.function.Function;

@AllArgsConstructor
public class CssSelectorHttpRequestExtractor extends AbstractHttpRequestExtractor<Document, HTMLDocumentHttpResponseResult> {
    private final Function<Document, Set<String>> newHttpRequestExtractor;

    @Override
    protected Set<String> extractUrl(HTMLDocumentHttpResponseResult documentHttpResponseResult) {
        return newHttpRequestExtractor.apply(documentHttpResponseResult.getContent());
    }
}
