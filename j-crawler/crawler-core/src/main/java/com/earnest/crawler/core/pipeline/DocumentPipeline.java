package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.HttpResponseResult;
import org.jsoup.nodes.Document;

import java.util.function.Function;

public class DocumentPipeline<R> implements Pipeline<Document, R> {

    private final Function<Document, R> pipe;

    public DocumentPipeline(Function<Document, R> pipe) {
        this.pipe = pipe;
    }

    @Override
    public R pipe(HttpResponseResult<Document> result) {
        return pipe.apply(result.getContent());
    }
}
