package com.earnest.crawler.core.proxy;


import com.earnest.crawler.core.Browser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FixedHttpProxyProvider extends AbstractHttpProxyProvider implements Closeable {

    public static final FixedHttpProxyProvider INSTANCE = new FixedHttpProxyProvider();

    private static final String PROXY_POOL_URL = "http://123.207.35.36:5010/get";

    private static final String COLON = ":";

    private final ResponseHandler<String> stringResponseHandler = new BasicResponseHandler();

    private final HttpClient httpClient = HttpClients
            .custom()
            .setUserAgent(Browser.GOOGLE.userAgent())
            .setMaxConnTotal(5)
            .build();


    private static HttpProxy convertToHttpProxy(String proxy) {
        String[] strings = StringUtils.split(proxy, COLON);
        return new HttpProxy(new HttpHost(strings[0], Integer.parseInt(strings[1])));
    }


    @Override
    public void close() {
        HttpClientUtils.closeQuietly(httpClient);
    }


    /**
     * 一次性的获取5个代理IP。
     */
    @Override
    protected void doInitializeHttpProxyPool() {
        HttpGet get = new HttpGet(PROXY_POOL_URL);
        //计数器
        CountDownLatch count = new CountDownLatch(5);

        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //异步获取5个代理连接
        IntStream.range(0, 5).forEach(s ->
                threadPool.execute(() -> {
                    //设置
                    get().map(a -> RequestConfig.custom().setProxy(a.getHttpHost()).build())
                            .ifPresent(get::setConfig);

                    //开始请求
                    try {
                        String proxy = httpClient.execute(get, stringResponseHandler);
                        log.info("Get a proxy address:{}", proxy);
                        putHttpProxy(convertToHttpProxy(proxy));
                        count.countDown();
                    } catch (IOException ignore) {

                    }
                })
        );
        try {
            //等待释放资源
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
            //关闭client
            close();
        }
    }
}
