package com.earnest.video.episode;

import com.earnest.video.entity.Episode;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class IQiYiEpisodeFetcherTest {

    IQiYiEpisodeFetcher episodeFetcher = new IQiYiEpisodeFetcher(HttpClients.createDefault(), new BasicResponseHandler());

    @Test
    public void fetch() throws Exception {

        List<Episode> episodes = episodeFetcher.fetch("http://www.iqiyi.com/a_19rrh9f0tx.html", null);

        episodes.stream()
                .map(Episode::getTitle)
                .forEach(System.out::println);

        Assert.assertNotNull(episodes);
    }


}