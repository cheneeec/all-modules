package com.earnest.crawler.builder;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.Browser;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Connection;

import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUriRequestConfigurer extends SharedSpiderConfigurer {
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
    void setBuilder(SpiderBuilder builder) {
        super.setBuilder(builder);
        this.builder = builder;
    }


    public static class HttpUriRequestPropertyConfigurer extends RequestConfigConfigurer{

        private final RequestBuilder requestBuilder;

        private String stringParam;

        private String contentType;

        private Map<String, String> parameters = new LinkedHashMap<>();

        private static Pattern charsetPattern = Pattern.compile(";charset=(.+)[;?]");

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
            this.parameters.put(name, value);
            requestBuilder.addParameter(name, value);
            return this;
        }

        public HttpUriRequestPropertyConfigurer addStringParamter(Object param) {
            this.stringParam = JSONObject.toJSONString(param);
            return this;
        }

        public HttpUriRequestPropertyConfigurer addParameters(Map<String, String> parameters) {
            this.parameters.putAll(parameters);
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
            this.contentType = StringUtils.deleteWhitespace(contentType);
            requestBuilder.setHeader(Browser.CONTENT_TYPE, this.contentType);
            return this;
        }


        @Override
        @SuppressWarnings("unchecked")
        void configure() {

            RequestBuilder httpUriRequestBuilder =
                    requestBuilder
                            .setConfig(getRequestConfigBuilder().build());


            //如果是contentType为json
            if (StringUtils.indexOf(contentType, ContentType.APPLICATION_JSON.getMimeType()) > -1 && stringParam != null) {
                //获取字符编码
                StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(stringParam), extractCharset(contentType));
                requestBuilder.setEntity(stringEntity);
            }


            List<Object> objects =
                    (List<Object>) sharedObjectMap.computeIfAbsent(HttpUriRequest.class, k -> new ArrayList<>());

            objects.add(0, httpUriRequestBuilder.build());

        }


        private static String extractCharset(String contentType) {
            Matcher matcher = charsetPattern.matcher(contentType);

            if (matcher.find()) {
                return matcher.group(1);
            }

            return Charset.defaultCharset().displayName();
        }


    }


}
