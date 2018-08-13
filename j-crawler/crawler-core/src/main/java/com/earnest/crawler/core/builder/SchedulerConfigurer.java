package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.extractor.EmptyHttpRequestExtractor;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.scheduler1.BlockingUniqueScheduler;
import com.earnest.crawler.core.scheduler1.FixedScheduler;
import com.earnest.crawler.core.scheduler1.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
public class SchedulerConfigurer extends SharedSpiderConfigurer<Scheduler> {

    private Scheduler scheduler;


    public SchedulerConfigurer blockingUnique(int timeout) {
        scheduler = new BlockingUniqueScheduler(timeout);
        return this;
    }

    public SchedulerConfigurer blockingUnique() {
        return blockingUnique(0);
    }

    public SchedulerConfigurer fixed() {
        scheduler = new FixedScheduler();
        return this;
    }

    //需要在HttpUriRequestExtractorConfigurer后进行configure()。
    @Override
    protected int order() {
        return 4;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        //获取请求列表
        List<HttpUriRequest> httpUriRequests = (List<HttpUriRequest>) sharedObjectMap.remove(HttpUriRequest.class);
        Assert.state(!CollectionUtils.isEmpty(httpUriRequests), "httpUriRequest is empty");
        log.debug("Obtained the initial request list:{}", httpUriRequests.stream().map(HttpUriRequest::getURI).collect(toList()));

        if (scheduler == null) {
            //判断请求
            List<?> httpRequestExtractors = sharedObjectMap.get(HttpRequestExtractor.class);

            if (httpRequestExtractors.isEmpty() || (httpRequestExtractors.get(0) instanceof EmptyHttpRequestExtractor)) {
                //初始化固定的调度器
                fixed();
            } else {//默认初始化调度器
                blockingUnique();
            }

        }

        //加入请求列表
        scheduler.putAll(httpUriRequests);

        sharedObjectMap.put(Scheduler.class, Collections.singletonList(scheduler));
    }
}
