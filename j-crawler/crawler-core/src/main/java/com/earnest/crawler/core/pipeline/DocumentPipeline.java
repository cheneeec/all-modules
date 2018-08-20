package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.HttpClientResponseResult;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.StringResponseResult;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.Assert;

import java.util.function.Consumer;

public class DocumentPipeline implements Pipeline {

    private final Consumer<HttpResponseResult<Document>> pipe;

    public DocumentPipeline(Consumer<HttpResponseResult<Document>> pipe) {
        Assert.notNull(pipe, "pipe is null");
        this.pipe = pipe;
    }


    @Override
    public void pipe(StringResponseResult result) {

        HttpUriRequest httpRequest = result.getHttpRequest();
        String url = "";
        if (httpRequest != null) {
            url = httpRequest.getRequestLine().getUri();
        }

        HttpClientResponseResult<Document> responseResult = new HttpClientResponseResult<>(result);


        responseResult.setContent(Jsoup.parse(result.getContent(), url));


        pipe.accept(responseResult);
    }
}
