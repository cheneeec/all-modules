package com.earnest.crawler.core.parser;

import com.earnest.crawler.IQiYi;
import com.earnest.crawler.core.downloader.Downloader;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.handler.RegexHttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;


public class ParserTest {
    private final Parser parser = new JsonConfigurationParser("crawler/iqiyi.json");

    @Test
    public void getHttpResponseHandler() {
        HttpResponseHandler responseHandler = parser.getHttpResponseHandler();
        Assert.assertEquals(responseHandler,new RegexHttpResponseHandler("/www/4/38-------------4-\\d-1-iqiyi--.html"));
    }

    @Test
    public void getPipeline() {
        Pipeline<IQiYi> pipeline = parser.getPipeline();
        Assert.assertNotNull(pipeline);
    }

    @Test
    public void getHttpRequests() {
        Set<HttpRequest> requests = parser.getHttpRequests();
        Assert.assertTrue(requests.size() == 1);
        Assert.assertEquals(requests.iterator().next().getUrl(), "http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html");
    }

    @Test
    public void getDownloader() {


    }
}