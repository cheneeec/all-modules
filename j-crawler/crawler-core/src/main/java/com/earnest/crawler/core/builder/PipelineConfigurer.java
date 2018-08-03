package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.pipeline.Pipeline;


public class PipelineConfigurer<T,R> extends AbstractSpiderConfigurer<Pipeline<T,R>> {

    private Pipeline<T,R> pipeline;


    public PipelineConfigurer(SpiderBuilder builder) {
        super(builder);
    }

    public PipelineConfigurer pipeline(Pipeline<T,R> pipeline) {
        this.pipeline = pipeline;
        return this;
    }


    @Override
    Pipeline<T, R> build() {

        return pipeline;
    }

}
