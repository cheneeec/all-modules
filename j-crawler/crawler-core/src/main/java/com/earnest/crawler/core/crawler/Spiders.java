package com.earnest.crawler.core.crawler;


import com.earnest.crawler.core.parser.ClassAnnotatedParser;
import com.earnest.crawler.core.parser.JsonConfigurationParser;
import com.earnest.crawler.core.parser.Parser;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;
import java.util.function.Consumer;

import static org.springframework.util.CollectionUtils.isEmpty;


@Slf4j
public class Spiders {
    Spiders() {
    }

    /**
     * 用户自定义创建。
     *
     * @return {@link SpiderBuilder}
     */
    public static SpiderBuilder createCustom() {
        return SpiderBuilder.create();
    }

    /**
     * 根据<code>json</code>的配置创建爬虫。
     *
     * @return Spider
     * @see JsonConfigurationParser
     */
    public static Spider createJsonConfigurable(String jsonConfigurationFilePath) {
        return extractParsedSpider(new JsonConfigurationParser(jsonConfigurationFilePath));
    }

    /**
     * 根据类上的注解来创建。
     *
     * @param clazz 该类必须带上特定的注解。
     * @param <T>
     * @return Spider
     * @see ClassAnnotatedParser
     */
    public static <T> Spider createClassAnnotated(Class<T> clazz) {
        //TODO 待开发
        throw new UnsupportedOperationException();
//        return extractParsedSpider(new ClassAnnotatedParser(clazz));

    }

    private static Spider extractParsedSpider(Parser parser) {
        SpiderBuilder spiderBuilder = createCustom();
        //set HttpRequest
        Set<HttpRequest> httpRequests = parser.getHttpRequests();
        if (!isEmpty(httpRequests)) httpRequests.forEach(spiderBuilder::addRequest);
        //set consumer
        Set<Consumer> persistenceConsumers = parser.getPersistenceConsumers();
        if (!isEmpty(persistenceConsumers)) persistenceConsumers.forEach(spiderBuilder::addConsumer);
        //set ...
        SpiderBuilder setOtherSpiderBuilder = spiderBuilder
                .thread(parser.getThread())
                .pipeline(parser.getPipeline())
                .httpResponseHandler(parser.getHttpResponseHandler())
                .downloader(parser.getDownloader());
        //destroy...
        try {

            parser.close();
        } catch (IOException ignored) {
            //ignored
        }

        //build
        return setOtherSpiderBuilder
                .build();
    }


}
