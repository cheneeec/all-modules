package com.earnest.crawler.core.crawler;

import com.earnest.crawler.core.downloader.DownloadListener;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;

import java.util.function.Consumer;

public interface Spider extends Crawler {

    Spider thread(int num);

    Spider from(String url);

    Spider from(HttpRequest httpRequest);

    Spider addRequest(HttpRequest httpRequest);

    Spider downloader(Downloader downloader);

    Spider addDownloaderListener(DownloadListener downloadListener);

   <T>  Spider start();

    Spider match(String regex);

    Spider stop();

     Spider pipeline(Pipeline pipeline);

    <T> Spider addConsumer(Consumer<T> persistenceConsumer);

}
