package com.earnest.crawler.core.scheduler;

import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.request.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetUniqueSchedulerTest {

    private final Scheduler scheduler = new SetUniqueScheduler();

    @Before
    public void before() {
        scheduler.put(new HttpGetRequest("1"));
        scheduler.put(new HttpGetRequest("2"));
        scheduler.put(new HttpGetRequest("3"));
        scheduler.put(new HttpGetRequest("4"));
    }

    @Test
    public void isEmpty() {
        Assert.assertFalse(scheduler.isEmpty());

    }

    @Test
    public void take() {
        HttpRequest take = scheduler.take();
        boolean equals = StringUtils.equalsAnyIgnoreCase(take.getUrl(), "1", "2", "3", "4");
        Assert.assertTrue(equals);

    }

    @Test
    public void put() {
        boolean put = scheduler.put(new HttpGetRequest("5"));
        Assert.assertTrue(put);

    }
}