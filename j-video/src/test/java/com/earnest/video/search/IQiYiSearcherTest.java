package com.earnest.video.search;

import com.earnest.video.entity.IQiYi;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.data.domain.Page;

import java.io.IOException;

public class IQiYiSearcherTest {


    @Test
    public void iQiYiSearcher() throws IOException {

        IQiYiPlatformSearcher platformSearcher = new IQiYiPlatformSearcher();
        Page<IQiYi> search = platformSearcher.search("海贼王", null);
        search.getContent().stream()
                .map(IQiYi::getTitle)
                .forEach(System.out::println);

        Assert.assertTrue(!search.getContent().isEmpty());
    }

    @Test
    public void httpClient() throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();

        //上下文
        HttpClientContext clientContext = new HttpClientContext();
        //CookieStore
        CookieStore cookieStore = new BasicCookieStore();

        clientContext.setCookieStore(cookieStore);

        HttpGet get = new HttpGet("http://search.video.iqiyi.com/o?channel_name=&if=html5&pageNum=1&pageSize=20&limit=20&category=&timeLength=0&releaseDate=&key=%E6%B5%B7%E8%B4%BC%E7%8E%8B&start=2&threeCategory=&u=cupy97yt8x8fb6vy67k4yyo&qyid=cupy97yt8x8fb6vy67k4yyo&pu=&video_allow_3rd=1&intent_result_number=10&intent_category_type=1&vfrm=2-3-0-1&_=1535962850480&callback=Zepto1535962842856");

        String execute = httpClient.execute(get, new BasicResponseHandler(), clientContext);

        cookieStore.getCookies()
                .stream()
                .map(s -> s.getName() + ":" + s.getValue())
                .forEach(System.out::println);

        System.out.println("=========================");

        System.out.println(execute);
    }


}
