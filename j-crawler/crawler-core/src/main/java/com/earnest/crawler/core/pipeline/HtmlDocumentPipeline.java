package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.HTMLDocumentHttpResponseResult;
import org.jsoup.nodes.Document;

public interface HtmlDocumentPipeline<R> extends Pipeline<Document, HTMLDocumentHttpResponseResult, R> {
}
