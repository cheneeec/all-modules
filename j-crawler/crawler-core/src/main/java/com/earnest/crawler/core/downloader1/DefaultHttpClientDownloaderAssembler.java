package com.earnest.crawler.core.downloader1;


import org.apache.http.impl.client.CloseableHttpClient;

public class DefaultHttpClientDownloaderAssembler extends HttpClientDownloader implements HttpClientDownloaderAssembler {

    private int thread = 5;


    @Override
    public void setThread(int thread) {
        this.thread = thread;
    }

    @Override
    protected CloseableHttpClient initializeHttpClient() {
        System.out.println("DefaultHttpClientDownloaderAssembler");
        System.out.println(this.thread);
        return super.initializeHttpClient();
    }

    public static void main(String[] args) {
        DefaultHttpClientDownloaderAssembler httpClientDownloaderAssembler=new DefaultHttpClientDownloaderAssembler();

    }
}
