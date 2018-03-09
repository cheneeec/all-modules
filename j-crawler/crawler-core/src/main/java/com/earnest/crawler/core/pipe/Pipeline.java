package com.earnest.crawler.core.pipe;

import org.jsoup.nodes.Document;

import java.util.function.Function;

public interface Pipeline<T> {

    T pipe(Document document, Function<Document, T> pipeRule);

    T pipe(String htmlString, Function<Document, T> pipeRule);


}
