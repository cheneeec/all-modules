package com.earnest.crawler.core;

import org.jsoup.nodes.Document;

public class HTMLDocumentHttpResponseResult extends BasicHttpResponseResult<Document> {

    private final Document content;

    public HTMLDocumentHttpResponseResult(Document content) {
        this.content = content;
    }

    @Override
    public Document getContent() {
        return content;
    }
}
