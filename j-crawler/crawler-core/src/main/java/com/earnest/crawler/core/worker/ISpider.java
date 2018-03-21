package com.earnest.crawler.core.worker;

import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;

import java.util.function.Consumer;

public interface ISpider {

    ISpider thread(int num);

    ISpider from(String url);

    ISpider from(HttpRequest httpRequest);

    ISpider addRequest(HttpRequest httpRequest);

    ISpider downloader(Downloader downloader);

     <T> ISpider start();

    ISpider match(String regex);

    ISpider stop();

    <T> ISpider pipeline(Pipeline<T> pipeline);

    <T> ISpider addConsumer(Consumer<T> persistenceConsumer);

}
