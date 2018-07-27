package com.earnest.crawler.core.request;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


public class HttpUriRequestAdapter extends HttpEntityEnclosingRequestBase {


    private final HttpRequest httpRequest;

    public HttpUriRequestAdapter(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;

        //set URI
        setURI(URI.create(httpRequest.getUrl()));

        //set Header
        setHeaders(httpRequest.getHeaders());

        //set Parameters
        setHttpEntity(httpRequest);

        //set requestConfig
        setConfig(createRequestConfig(httpRequest));
        //================
    }

    private void setHttpEntity(HttpRequest httpRequest) {
        String charset = StringUtils.defaultString(httpRequest.getCharset(), "UTF-8");
        Map<String, String> parameters = httpRequest.getParameters();

        if (!StringUtils.equalsAnyIgnoreCase("GET", "DELETE")
                && !CollectionUtils.isEmpty(parameters)) {
            HttpEntity httpRequestEntity = null;
            //判断content-type
            Map<String, String> headers = httpRequest.getHeaders();
            String contentType = null;
            if (nonNull(headers)) {
                contentType = headers.getOrDefault("content-type", ContentType.APPLICATION_FORM_URLENCODED.toString());
            }

            //JSON
            if (StringUtils.equalsIgnoreCase(ContentType.APPLICATION_JSON.toString(), contentType)) {
                httpRequestEntity = new StringEntity(JSONObject.toJSONString(parameters), charset);
            } else {

                List<BasicNameValuePair> nameValuePairs = parameters.keySet().stream()
                        .map(k -> new BasicNameValuePair(k, parameters.get(k)))
                        .collect(Collectors.toList());

                try {
                    httpRequestEntity = new UrlEncodedFormEntity(nameValuePairs, charset);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }

            setEntity(httpRequestEntity);
        }
    }


    private static RequestConfig createRequestConfig(HttpRequest httpRequest) {
       /* RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
        //set Proxy
        HttpRequest.HttpProxy httpProxy = httpRequest.getHttpProxy();
        if (nonNull(httpProxy)) {
            requestConfigBuilder.setProxy(httpProxy.getHttpHost());
        }
        //set connectTimeout
        int connectTimeout = httpRequest.getConnectTimeout();
        if (connectTimeout != 0) {
            requestConfigBuilder.setConnectTimeout(connectTimeout);
        }

        return requestConfigBuilder.build();*/

        return null;
    }

    private void setHeaders(Map<String, String> headersMap) {
        if (!CollectionUtils.isEmpty(headersMap)) {
            Header[] headers = headersMap.keySet().stream()
                    .map(k -> new BasicHeader(k, headersMap.get(k)))
                    .toArray(Header[]::new);

            super.setHeaders(headers);
        }

    }

    @Override
    public String getMethod() {
        return httpRequest.getMethod();
    }

    public HttpContext obtainHttpContext() {
        HttpClientContext clientContext = HttpClientContext.create();

        //set Cookies
        Map<String, String> cookies = httpRequest.getCookies();
        if (!CollectionUtils.isEmpty(cookies)) {
            BasicCookieStore cookieStore = new BasicCookieStore();

            cookieStore.addCookies(
                    cookies.keySet().stream()
                            .map(k -> {
                                BasicClientCookie cookie = new BasicClientCookie(k, cookies.get(k));
                                if (StringUtils.isBlank(cookie.getDomain())) {
                                    cookie.setDomain(URI.create(httpRequest.getUrl()).getHost());
                                }
                                return cookie;
                            })
                            .toArray(Cookie[]::new)
            );

            clientContext.setCookieStore(cookieStore);
        }
        /*//set proxy Credentials
        HttpRequest.HttpProxy httpProxy = httpRequest.getHttpProxy();
        if (nonNull(httpProxy) && !StringUtils.isAnyBlank(httpProxy.getUsername()
                , httpProxy.getPassword())) {


        }*/


        return clientContext;
    }
}
