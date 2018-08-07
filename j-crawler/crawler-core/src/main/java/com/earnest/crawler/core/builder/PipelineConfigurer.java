package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.pipeline.Pipeline;
import org.jsoup.nodes.Document;

import java.util.function.Function;


public class PipelineConfigurer<T, R> extends AbstractSpiderConfigurer<Pipeline<T, R>> {

    private Pipeline<T, R> pipeline;


    public PipelineConfigurer(SpiderBuilder builder) {
        super(builder);
    }

    public PipelineConfigurer<T, R> pipeline(Pipeline<T, R> pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    /*@SuppressWarnings("unchecked")
    public PipelineConfigurer<T, R> asFile(Function<HttpResponseResult<InputStream>, R> fileHandler) {
        this.pipeline = t -> fileHandler.apply((HttpResponseResult<InputStream>) t);
        return this;
    }*/

    @SuppressWarnings("unchecked")
    public PipelineConfigurer<T, R> cssSelector(Function<HttpResponseResult<Document>, R> cssSelector) {
        this.pipeline = t -> cssSelector.apply((HttpResponseResult<Document>) t);
        return this;
    }


    @Override
    Pipeline<T, R> build() {

        return pipeline;
    }

}
