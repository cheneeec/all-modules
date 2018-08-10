package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.request.HttpProxy;
import org.apache.http.client.config.RequestConfig;

public class RequestConfigConfigurer<O> extends SharedSpiderConfigurer<O> {

    protected RequestConfig.Builder custom = RequestConfig.custom();



    public RequestConfigConfigurer<O> connectTimeout(int connectTimeout) {
        custom.setConnectTimeout(connectTimeout);
        return this;
    }

    public RequestConfigConfigurer<O> setProxy(HttpProxy proxy) {
        custom.setProxy(proxy.getHttpHost());
        return this;
    }

    protected RequestConfig requestConfig() {
        return custom.build();
    }


}
