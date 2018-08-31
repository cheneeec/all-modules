package com.earnest.video.parse;

import com.earnest.crawler.core.Browser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class IQiYiParse {
    private HttpClient httpClient = HttpClients.custom().setUserAgent(Browser.IPHONE_X.userAgent()).build();
    private BasicResponseHandler basicResponseHandler = new BasicResponseHandler();

    @Test
    public void t() throws IOException {
        //1.取得id=1259275400和web_id=1259275400
        String execute = httpClient.execute(new HttpGet("http://jiexi.071811.cc/jx2.php?url=http://www.iqiyi.com/v_19rqzi1f7s.html"), basicResponseHandler);
        System.out.println(execute);

    }

    @Test
    public void time(){
        //1535644800
        double v = 157248E5 + new Date().getTime();
        System.out.println(v);
    }
}
