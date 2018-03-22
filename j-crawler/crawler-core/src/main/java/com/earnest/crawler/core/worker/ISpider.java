package com.earnest.crawler.core.worker;

import com.earnest.crawler.core.downloader.DownloadListener;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;
import javafx.concurrent.Worker;

import java.util.function.Consumer;

public interface ISpider extends IWorker {

    ISpider thread(int num);

    ISpider from(String url);

    ISpider from(HttpRequest httpRequest);

    ISpider addRequest(HttpRequest httpRequest);

    ISpider downloader(Downloader downloader);

    ISpider addDownloaderListener(DownloadListener downloadListener);

    <T> ISpider start();

    ISpider match(String regex);

    ISpider stop();

    <T> ISpider pipeline(Pipeline<T> pipeline);

    <T> ISpider addConsumer(Consumer<T> persistenceConsumer);

}
