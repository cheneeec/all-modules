package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.scheduler1.BlockingUniqueScheduler;
import com.earnest.crawler.core.scheduler1.Scheduler;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class SchedulerConfigurer extends SharedSpiderConfigurer<Scheduler> {

    private Scheduler scheduler;


    public SchedulerConfigurer blockingUnique() {

        return this;

    }

    @Override
    protected int order() {
        return 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {

        List<HttpUriRequest> httpUriRequests = (List<HttpUriRequest>) sharedObjectMap.remove(HttpUriRequest.class);
        Assert.state(!CollectionUtils.isEmpty(httpUriRequests), "httpUriRequest is empty");

        scheduler.put(httpUriRequests);

        sharedObjectMap.put(Scheduler.class, Collections.singletonList(new BlockingUniqueScheduler()));
    }
}
