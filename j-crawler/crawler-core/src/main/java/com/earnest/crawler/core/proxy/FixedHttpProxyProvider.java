package com.earnest.crawler.core.proxy;


import com.earnest.crawler.core.Browser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.Assert;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class FixedHttpProxyProvider extends AbstractHttpProxyProvider implements Closeable {

//    public static final FixedHttpProxyProvider INSTANCE = new FixedHttpProxyProvider();

    private static final String DEFAULT_PROXY_POOL_URL = "http://123.207.35.36:5010/get";

    private static final String COLON = ":";
    //默认最大的代理数量
    public static final int DEFAULT_MAX_HTTP_PROXY_COUNT = 20;
    //默认检查
    public static final boolean DEFAULT_PERIODICALLY_CHECK = true;

    public static final int DEFAULT_THREAD_NUMBER = Runtime.getRuntime().availableProcessors();


    private final ResponseHandler<String> stringResponseHandler = new BasicResponseHandler();

    private final HttpUriRequest httpUriRequest;

    private final ScheduledExecutorService threadPool;

    private final AtomicInteger maxCount;

    private final boolean periodicallyCheck;

    private final HttpClient httpClient;

    private static CloseableHttpClient initializeHttpClient(int threadNumber) {
        return HttpClients
                .custom()
                .setUserAgent(Browser.GOOGLE.userAgent())
                .setMaxConnTotal(threadNumber)
                .build();
    }

    /**
     * @param httpUriRequest    请求的对象。
     * @param maxHttpProxyCount 获取{@link HttpProxy }的最大上限。
     * @param thread            线程数。
     * @param periodicallyCheck 是否定期移除无效的{@link HttpProxy}。
     */
    public FixedHttpProxyProvider(HttpUriRequest httpUriRequest, int maxHttpProxyCount, int thread, boolean periodicallyCheck) {
        Assert.notNull(httpUriRequest, "httpUriRequest is required");
        this.httpUriRequest = httpUriRequest;
        maxCount = new AtomicInteger(maxHttpProxyCount);
        threadPool = Executors.newScheduledThreadPool(thread);
        this.periodicallyCheck = periodicallyCheck;
        this.httpClient = initializeHttpClient(thread);
    }

    public FixedHttpProxyProvider(String url, int maxHttpProxyCount, int thread, boolean periodicallyCheck) {
        this(new HttpGet(url), maxHttpProxyCount, thread, periodicallyCheck);
    }

    public FixedHttpProxyProvider(String url) {
        this(url, DEFAULT_MAX_HTTP_PROXY_COUNT, DEFAULT_THREAD_NUMBER, DEFAULT_PERIODICALLY_CHECK);
    }

    public FixedHttpProxyProvider(){
        this(DEFAULT_PROXY_POOL_URL);
    }


    private static HttpProxy convertToHttpProxy(String proxy) {
        String[] strings = StringUtils.split(proxy, COLON);
        return new HttpProxy(new HttpHost(strings[0], Integer.parseInt(strings[1])));
    }


    @Override
    public void close() {
        try {
            HttpClientUtils.closeQuietly(httpClient);
        } finally {
            if (!threadPool.isShutdown()) {
                threadPool.shutdown();
            }
        }

    }


    /**
     * 在初始化的时候一次性的获取5个代理IP。
     */
    @Override
    protected void doInitializeHttpProxyPool() {

        HttpGet get = new HttpGet(DEFAULT_PROXY_POOL_URL);

//        HttpUriRequest httpUriRequest = this.httpUriRequest;
        //计数器
        CountDownLatch count = new CountDownLatch(5);

        //异步获取5个代理连接
        IntStream.range(0, 5).forEach(s ->
                threadPool.execute(() -> {
                    //设置
                    get().map(a -> RequestConfig.custom().setProxy(a.getHttpHost()).build())
                            .ifPresent(get::setConfig);

                    /*get().map(a -> RequestConfig.custom().setProxy(a.getHttpHost()).build())
                            .map(c -> RequestBuilder.copy(httpUriRequest).setConfig(c).build())
                            .ifPresent(config ->
                                    RequestBuilder.copy(httpUriRequest).setConfig(config).build();
                            )*/

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
            //释放资源
            close();
        }
    }



    /**
     * 剔除无效的代理数据。
     */
    private void periodicallyDeleteInvalidHttpProxy() {

    }

}
