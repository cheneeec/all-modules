package com.earnest.video.search;

import com.earnest.video.core.search.IQiYiPlatformHttpClientSearcher;
import com.earnest.video.entity.IQiYi;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;

import static org.junit.Assert.*;

public class IQiYiPlatformHttpClientSearcherTest {

    private final IQiYiPlatformHttpClientSearcher httpClientSearcher
            = new IQiYiPlatformHttpClientSearcher(HttpClients.createDefault(), new BasicResponseHandler());

    @Test
    public void search() throws IOException {
        Page<IQiYi> iQiYis = httpClientSearcher.search("海贼王", new PageRequest(0, 20));
        assertTrue(iQiYis.getTotalElements() > 0);
        assertTrue(!iQiYis.getContent().isEmpty());
    }
}