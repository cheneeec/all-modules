package com.earnest.crawler.core.crawler;


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

    public static SpiderBuilder createCustom() {
        return SpiderBuilder.create();
    }

    public static Spider createJsonConfigurable(String jsonConfigurationFilePath) {

        Parser parser = new JsonConfigurationParser(jsonConfigurationFilePath);
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

    public static Spider copyFrom(Spider spider) {

        return null;
    }

}
