package com.earnest.crawler.core.builder;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.earnest.crawler.core.HttpResponseResult;
import com.earnest.crawler.core.pipeline.DocumentPipeline;
import com.earnest.crawler.core.pipeline.FileNameGenerator;
import com.earnest.crawler.core.pipeline.FilePipeline;
import com.earnest.crawler.core.pipeline.Pipeline;
import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 配置相关的{@link Pipeline}。如果没有指定将配置一个控制台输出的管道。
 */
public class PipelineConfigurer extends SharedSpiderConfigurer<Pipeline> {

    private Pipeline pipeline;


    public PipelineConfigurer pipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
        return this;
    }


    @SuppressWarnings("unchecked")
    public PipelineConfigurer cssSelector(Consumer<HttpResponseResult<Document>> cssSelector) {
        this.pipeline = new DocumentPipeline(cssSelector);
        return this;
    }

    /**
     * 将下载内容转化为文件进行存储。
     *
     * @param rootPath          保存文件的根路径。
     * @param charset           字符集。默认为UTF-8。
     * @param fileNameGenerator 定义保存文件的名字。默认为请求地址的路径{@link com.earnest.crawler.core.pipeline.UriPathFileNameGenerator}。
     * @return
     */
    public PipelineConfigurer asFile(String rootPath, String charset, FileNameGenerator fileNameGenerator) {
        this.pipeline = new FilePipeline(rootPath, charset);
        ((FilePipeline) this.pipeline).setFileNameGenerator(fileNameGenerator);
        return this;
    }

    /**
     * @param rootPath
     * @param charset
     * @return
     * @see #asFile(String, String, FileNameGenerator)
     */
    public PipelineConfigurer asFile(String rootPath, String charset) {
        this.pipeline = new FilePipeline(rootPath, charset);
        return this;
    }

    /**
     * @param rootPath
     * @return
     * @see #asFile(String, String, FileNameGenerator)
     */
    public PipelineConfigurer asFile(String rootPath) {
        this.pipeline = new FilePipeline(rootPath);
        return this;
    }

    @Override
    public void configure() {

        sharedObjectMap.put(Pipeline.class,
                Collections.singletonList(
                        Optional.ofNullable(pipeline)
                                .orElse(r -> System.out.println(
                                        JSONObject.toJSONString(r, SerializerFeature.PrettyFormat)
                                ))
                ));
    }


}
