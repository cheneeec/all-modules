package com.earnest.crawler.core.builder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class SharedSpiderConfigurer<O> implements Comparable<SharedSpiderConfigurer<O>>, Configurer {

    private SpiderBuilder builder;

   /* //存放所有的请求
    protected final List<HttpUriRequest> httpUriRequests = new ArrayList<>();

    //存放全局的cookieStore
    protected final CookieStore globalCookieStore = new BasicCookieStore();

    //存放会话的Cookies
    protected final CookieStore httpContextCookieStore = new BasicCookieStore();

    //存放会话的上下文
    protected final HttpClientContext httpClientContext = new HttpClientContext();*/

    @Setter(value = AccessLevel.PACKAGE)
    protected Map<Class<?>, List<? extends Object>> sharedObjectMap;

    protected Integer thread;


    public SpiderBuilder and() {
        Assert.state(builder != null, "spider builder is null");

        return builder;
    }


    protected int order() {
        return Integer.MAX_VALUE;
    }


    @Override
    public int compareTo(SharedSpiderConfigurer<O> o) {

        return Integer.compare(order(), o.order());
    }

    @Override
    public void init()  {

    }

    @Override
    public void configure() {

    }

    @SuppressWarnings("unchecked")
    <T> List<T> get(Class<T> requiredClass) {
        return (List<T>) sharedObjectMap.get(requiredClass);
    }
}
