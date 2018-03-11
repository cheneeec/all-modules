package com.earnest.crawler.core.pipe;

import com.earnest.crawler.core.response.HttpResponse;
import org.jsoup.nodes.Document;

public class CoarsePipeline implements Pipeline<HttpResponse, Document> {

    @Override
    public Document pipe(HttpResponse httpResponse) {
        return null;
    }
}
