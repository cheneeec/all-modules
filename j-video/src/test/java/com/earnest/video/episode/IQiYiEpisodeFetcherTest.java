package com.earnest.video.episode;

import com.earnest.video.bean.CloseableHttpClientFactoryBean;
import com.earnest.video.entity.Episode;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class IQiYiEpisodeFetcherTest {
    IQiYiEpisodeFetcher episodeFetcher = new IQiYiEpisodeFetcher(CloseableHttpClientFactoryBean.INSTANCE.getObject());

    @Test
    public void fetch() throws Exception {

        List<Episode> episodes = episodeFetcher.fetch("http://www.iqiyi.com/a_19rrh9f0tx.html", null);

        episodes.stream()
                .map(Episode::getTitle)
                .forEach(System.out::println);

        Assert.assertNotNull(episodes);
    }


}