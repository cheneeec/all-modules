package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.Browser;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Connection;

import java.util.*;

public class HttpUriRequestConfigurer extends SharedSpiderConfigurer<HttpUriRequest> {
    private SpiderBuilder builder;
    private HttpUriRequestPropertyConfigurer httpUriRequestPropertyConfigurer;


    public HttpUriRequestPropertyConfigurer method(Connection.Method method) {
        RequestBuilder requestBuilder = RequestBuilder.create(method.name());
        httpUriRequestPropertyConfigurer = new HttpUriRequestPropertyConfigurer(requestBuilder, builder);
        httpUriRequestPropertyConfigurer.setSharedObjectMap(sharedObjectMap);
        return httpUriRequestPropertyConfigurer;
    }

    @Override
    protected int order() {
        return 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    void init() {
        //放入会话CookieStore
        List<Object> cookieStores = (List<Object>) sharedObjectMap.get(CookieStore.class);
        cookieStores.add(new BasicCookieStore());
    }

    @Override
    void configure() {
        httpUriRequestPropertyConfigurer.configure();
    }

    @Override
    public void setBuilder(SpiderBuilder builder) {
        super.setBuilder(builder);
        this.builder = builder;
    }


    public class HttpUriRequestPropertyConfigurer extends RequestConfigConfigurer<HttpUriRequest> {

        private final RequestBuilder requestBuilder;

        private HttpUriRequestPropertyConfigurer(RequestBuilder requestBuilder, SpiderBuilder builder) {
            this.requestBuilder = requestBuilder;
            setBuilder(builder);
        }

        public HttpUriRequestPropertyConfigurer from(String url) {
            requestBuilder.setUri(url);
            return this;
        }

        //
        public HttpUriRequestPropertyConfigurer addCookie(String name, String value) {
            Object o = sharedObjectMap.get(CookieStore.class).get(1);
            if (o instanceof CookieStore) {
                ((CookieStore) o).addCookie(new BasicClientCookie(name, value));
            }
            return this;
        }

        public HttpUriRequestPropertyConfigurer addCookies(Map<String, String> cookies) {
            Object o = sharedObjectMap.get(CookieStore.class).get(1);
            if (o instanceof CookieStore) {
                cookies.forEach((k, v) -> ((CookieStore) o).addCookie(new BasicClientCookie(k, v)));

            }
            return this;
        }

        public HttpUriRequestPropertyConfigurer addParameter(String name, String value) {
            requestBuilder.addParameter(name, value);
            return this;
        }

        public HttpUriRequestPropertyConfigurer addParameters(Map<String, String> parameters) {
            parameters.forEach(requestBuilder::addParameter);
            return this;
        }

        public HttpUriRequestPropertyConfigurer referer(String referer) {
            requestBuilder.setHeader(Browser.REFERER, referer);
            return this;
        }

        public HttpUriRequestPropertyConfigurer userAgent(String userAgent) {
            requestBuilder.setHeader(Browser.USER_AGENT, userAgent);
            return this;
        }


        public HttpUriRequestPropertyConfigurer contentType(String contentType) {
            requestBuilder.setHeader(Browser.CONTENT_TYPE, contentType);
            return this;
        }


        @Override
        @SuppressWarnings("unchecked")
         void configure() {

            HttpUriRequest httpUriRequest = requestBuilder.build();

            List<Object> objects =
                    (List<Object>) sharedObjectMap.computeIfAbsent(HttpUriRequest.class, k -> new ArrayList<>());

            objects.add(0, httpUriRequest);

        }
    }


}
