package com.earnest.crawler.proxy;

import com.alibaba.fastjson.util.IOUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.util.Assert;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;


@Slf4j
public class HttpProxyPoolSettingSupport implements HttpProxyPoolAware, Closeable {

    @Getter
    private HttpProxySupplier httpProxySupplier;

    @Setter
    @Getter
    protected boolean useHttpProxy = true;



    @Override
    public void setHttpProxySupplier(HttpProxySupplier httpProxySupplier) {
        this.httpProxySupplier = httpProxySupplier;
    }




    /**
     * 检查{@link HttpProxyPoolSettingSupport#httpProxySupplier}是否可用。
     */
    private static void checkHttpSupplierAvailable() {

    }

    public HttpUriRequest addHttpProxySetting(HttpUriRequest httpUriRequest) {
        if (!useHttpProxy) {
            return httpUriRequest;
        }

        Assert.notNull(httpUriRequest, "httpUriRequest is null");
        if (httpUriRequest instanceof HttpRequestBase) {
            RequestConfig requestConfig = addProxyHttpHost(((HttpRequestBase) httpUriRequest).getConfig());
            ((HttpRequestBase) httpUriRequest).setConfig(requestConfig);
            return httpUriRequest;
        } else {
            RequestBuilder requestBuilder = RequestBuilder.copy(httpUriRequest);
            return requestBuilder.setConfig(addProxyHttpHost(requestBuilder.getConfig())).build();
        }
    }


    public void addHttpProxySetting(RequestBuilder requestBuilder) {
        if (!useHttpProxy || httpProxySupplier == null) {
            return;
        }
        Assert.notNull(requestBuilder, "requestBuilder is null");
        requestBuilder.setConfig(addProxyHttpHost(requestBuilder.getConfig()));

        if (requestBuilder.getConfig().getProxy() != null) {
            log.debug("Set proxy address:{} for request {}", requestBuilder.getConfig().getProxy(), requestBuilder.getUri());
        }

    }

    private RequestConfig addProxyHttpHost(RequestConfig requestConfig) {

        RequestConfig.Builder builder = Optional.ofNullable(requestConfig)
                .map(RequestConfig::copy)
                .orElse(RequestConfig.custom());

        //为其设置代理
        httpProxySupplier.get()
                .map(HttpProxy::getHttpHost)
                .ifPresent(builder::setProxy);


        return builder.build();

    }


    @Override
    public void close() throws IOException {
        IOUtils.close(httpProxySupplier);

    }
}
