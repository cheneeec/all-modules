package com.earnest.crawler.core.downloader1;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class DefaultHttpClientDownloaderAssembler  implements HttpClientDownloaderAssembler {

    private int thread = 5;




    @Override
    public void setThread(int thread) {
        this.thread = thread;
    }
}
