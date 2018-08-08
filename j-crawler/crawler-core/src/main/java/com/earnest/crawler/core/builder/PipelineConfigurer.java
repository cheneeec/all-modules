package com.earnest.crawler.core.builder;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.pipeline.DocumentPipeline;
import com.earnest.crawler.core.pipeline.Pipeline;
import org.jsoup.nodes.Document;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;


public class PipelineConfigurer extends AbstractSpiderConfigurer<Pipeline> {

    private Pipeline pipeline;


    public PipelineConfigurer(SpiderBuilder builder) {
        super(builder);
    }

    public PipelineConfigurer pipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
        return this;
    }


    @SuppressWarnings("unchecked")
    public PipelineConfigurer cssSelector(Consumer<HttpResponseResult<Document>> cssSelector) {
        this.pipeline = new DocumentPipeline(cssSelector);
        return this;
    }


    @Override
    Pipeline build() {

        return Optional.ofNullable(pipeline)
                .orElse(r -> System.out.println(
                        JSONObject.toJSONString(r, SerializerFeature.PrettyFormat)
                ));
    }

}
