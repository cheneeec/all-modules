package com.earnest.crawler.builder;

import com.earnest.crawler.proxy.HttpProxy;
import org.apache.http.client.config.RequestConfig;

public class RequestConfigConfigurer extends SharedSpiderConfigurer {

    protected RequestConfig.Builder custom = RequestConfig.custom();


    public RequestConfigConfigurer connectTimeout(int connectTimeout) {
        custom.setConnectTimeout(connectTimeout);
        return this;
    }

    public RequestConfigConfigurer setProxy(HttpProxy proxy) {
        custom.setProxy(proxy.getHttpHost());
        return this;
    }

    protected RequestConfig.Builder getRequestConfigBuilder() {
        return custom;
    }


}
