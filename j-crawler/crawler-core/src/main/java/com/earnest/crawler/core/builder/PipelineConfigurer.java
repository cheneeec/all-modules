package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.pipeline.Pipeline;

import java.util.LinkedHashMap;
import java.util.Map;

public class PipelineConfigurer extends AbstractSpiderConfigurer<Map<String, Pipeline>> {

    private Pipeline defaultPipeline;

    private Map<String, Pipeline> pipelines = new LinkedHashMap<>(5);

    public PipelineConfigurer(SpiderBuilder builder) {
        super(builder);
    }

    public PipelineConfigurer defaultPipeline(Pipeline pipeline) {
        this.defaultPipeline = pipeline;
        return this;
    }

    public PipelineConfigurer addPipeline(String pattern, Pipeline pipeline) {
        pipelines.put(pattern, pipeline);
        return this;
    }

    @Override
    Map<String, Pipeline> build() {
        if (defaultPipeline != null) {
            pipelines.put("**", defaultPipeline);
        }
        return pipelines;
    }

}
