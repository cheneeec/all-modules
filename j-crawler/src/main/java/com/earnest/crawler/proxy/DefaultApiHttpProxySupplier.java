package com.earnest.crawler.proxy;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;


/**
 * 使用默认的API获取代理的连接。
 */
@Slf4j
public class DefaultApiHttpProxySupplier implements HttpProxySupplier {

    private static final String COLON = ":";

    private final String apiAddress;

    private static final String GET_SUFFIX = "/get";

    private static final String STATUS_SUFFIX = "/get_status";

    private final HttpClient httpClient;

    private final ResponseHandler<String> responseHandler;




    private static HttpProxy convertToHttpProxy(String proxy) {
        String[] strings = StringUtils.split(proxy, COLON);
        return new HttpProxy(new HttpHost(strings[0], Integer.parseInt(strings[1])));
    }

    public DefaultApiHttpProxySupplier(String apiAddress, @Nullable HttpClient httpClient, @Nullable ResponseHandler<String> responseHandler) {
        Assert.hasText(apiAddress, "apiAddress is required");
        if (StringUtils.endsWith(apiAddress, "\\/")) {
            apiAddress = StringUtils.removeEnd(apiAddress, "\\/");
        }
        this.httpClient = defaultIfNull(httpClient, HttpClients.createDefault());
        this.responseHandler = defaultIfNull(responseHandler, new BasicResponseHandler());
        this.apiAddress = apiAddress;
    }


    @Override
    public void close() {
        HttpClientUtils.closeQuietly(httpClient);
    }


    /**
     * 在初始化的时候一次性的获取5个代理IP。
     */

    protected void doInitializeHttpProxyPool() throws Exception {

        /*HttpGet get = new HttpGet(DEFAULT_PROXY_POOL_URL);

        //异步获取5个代理连接
        IntStream.range(0, 3).forEach(s ->
                threadPool.execute(() -> {
                    //设置
                    get().map(a -> RequestConfig.custom().setProxy(a.getHttpHost()).build())
                            .ifPresent(get::setConfig);

                    //开始请求
                    try {
                        String proxy = httpClient.execute(get, stringResponseHandler);
                        log.info("Get a proxy address:{}", proxy);
                        putHttpProxy(convertToHttpProxy(proxy));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                })
        );
        close();
        try {
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }*/
    }


    @Override
    public boolean hasAvailableProxy() {
        try {
            return JSONObject.parseObject(
                    httpClient.execute(
                            new HttpGet(apiAddress + STATUS_SUFFIX), responseHandler
                    )
            )
                    .getIntValue("useful_proxy") > 0;
        } catch (IOException e) {
            throw new IllegalStateException("connect error:" + e.getMessage(), e);
        }
    }

    @Override
    public void remove(@Nullable HttpProxy httpProxy) {
        if (httpProxy == null) {
            return;
        }
        HttpHost httpHost = httpProxy.getHttpHost();
        HttpGet get = new HttpGet(apiAddress + "delete?proxy=" + httpHost.getHostName() + ":" + httpHost.getPort());
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            HttpClientUtils.closeQuietly(httpResponse);
        } catch (IOException ignore) {
            //ignore
        }
    }

    @Override
    public Optional<HttpProxy> get() {
        HttpGet httpGet = new HttpGet(apiAddress + GET_SUFFIX);
        try {
            String result = httpClient.execute(httpGet, responseHandler);
            return Optional.of(convertToHttpProxy(result));
        } catch (IOException e) {
            throw new IllegalStateException("connect error:" + e.getMessage(), e);
        }
    }


}
