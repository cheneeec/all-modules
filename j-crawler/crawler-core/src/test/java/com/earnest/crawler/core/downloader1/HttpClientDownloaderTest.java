package com.earnest.crawler.core.downloader1;

import com.earnest.crawler.core.HtmlResponsePage;
import com.earnest.crawler.core.request.HttpGetRequest;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;


public class HttpClientDownloaderTest {

    HttpClientDownloader downloader=new HttpClientDownloader(HttpClients.createDefault());

    @Test
    public void download() throws IOException {
        HtmlResponsePage download = downloader.download(new HttpGetRequest("http://www.xicidaili.com/nn/1"));


    }
}