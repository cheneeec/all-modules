package com.earnest.example;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;


import java.io.IOException;
import java.net.URI;

public class HttpClientExample {
    @Test
    public void proxyAndCredentials() throws Exception {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpHost targetHost = new HttpHost("localhost", 80, "http");
        CredentialsProvider credsProvider = new BasicCredentialsProvider();

        credsProvider.setCredentials(
                new AuthScope(targetHost.getHostName(), targetHost.getPort()),
                new UsernamePasswordCredentials("username", "password"));

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(targetHost, basicAuth);

        // Add AuthCache to the execution context
        HttpClientContext context = HttpClientContext.create();
        context.setCredentialsProvider(credsProvider);
        context.setAuthCache(authCache);

        HttpGet httpget = new HttpGet("/");
        for (int i = 0; i < 3; i++) {
            try (CloseableHttpResponse response = httpClient.execute(
                    targetHost, httpget, context)) {
                HttpEntity entity = response.getEntity();

            }
        }
    }

    @Test
    public void httpGet() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpUriRequest request = RequestBuilder.get(URI.create("https://github.com/fengzhizi715/ProxyPool"))
                .setConfig(RequestConfig.custom()
                        .setProxy(HttpHost.create("http://14.118.252.64:6666"))
                        .build())
                .build();
        CloseableHttpResponse httpResponse = httpClient.execute(request);
        Assert.assertTrue(EntityUtils.toString(httpResponse.getEntity()).length() > 0);
    }

    @Test
    public void ping() throws IOException {

    }

    @Test
    public void rest() throws IOException {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore).build();

        HttpGet httpGet = new HttpGet("https://www.52pojie.cn/forum-24-1.html");

        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpGet);

        System.out.println("======================");
        cookieStore.getCookies()
                .forEach(cookie -> System.out.println(cookie.getName()+":"+cookie.getValue()));

    }
}
