package com.earnest.script;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.core.Browser;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class HtmlunitTest {

    private final HttpClient httpClient = HttpClients.custom().setUserAgent(Browser.GOOGLE.userAgent()).build();

    @Test(timeout = Long.MAX_VALUE)
    public void show() throws Exception {

        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        WebClientOptions webClientOptions = webClient.getOptions();

        //禁用CSS
        webClientOptions.setCssEnabled(false);
        //禁用javascript支持
        webClientOptions.setJavaScriptEnabled(true);//启用JS解释器，默认为true
        webClientOptions.setPrintContentOnFailingStatusCode(false);//在失败的时候打印内容
        webClientOptions.setDoNotTrackEnabled(true);
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);///当HTTP的状态非200时是否抛出异常
        webClientOptions.setActiveXNative(false); //默认false

        webClientOptions.setThrowExceptionOnScriptError(false);//但js执行错误时，是否抛出异常
        webClientOptions.setTimeout(5000);//设置“浏览器”的请求超时时间

        webClient.waitForBackgroundJavaScript(5000);//设置JS后台等待执行时间


//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持ajax

        final WebRequest[] ajax = new WebRequest[1];


        webClient.setAjaxController(new AjaxController() {
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
                System.out.println(request.getUrl());
                System.out.println(request.getRequestBody());
                System.out.println(request.getHttpMethod());
                ajax[0] = request;
                return super.processSynchron(page, request, async);
            }
        });





        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(WebWindowEvent event) {
            }

            @Override
            public void webWindowContentChanged(WebWindowEvent event) {

                System.out.println(event.getNewPage().getUrl());
                if ("http://jiexi.071811.cc/stapi.php?url=http://www.iqiyi.com/v_19rqz6uit0.html".equals(event.getNewPage().getUrl().toString())) {
                    WebResponse webResponse = event.getNewPage().getWebResponse();
                    System.out.println(webResponse.getContentAsString());
                }

                JavaScriptJobManager jobManager = event.getWebWindow().getJobManager();



                jobManager.removeAllJobs();


                if ("http://jiexi.071811.cc/api/xit.php".equalsIgnoreCase(event.getNewPage().getUrl().toString())) {
                    System.out.println(event.getNewPage().getWebResponse().getContentAsString());
                }

                // http://jiexi.071811.cc/api/xit.php

                if (event.getOldPage() != null) {
                    System.out.println(event.getOldPage().getUrl());
                }

            }

            @Override
            public void webWindowClosed(WebWindowEvent event) {

            }
        });

        HtmlPage page = webClient.getPage("http://jiexi.071811.cc/jx2.php?url=http://www.iqiyi.com/v_19rqz6uit0.html");
   /*     System.out.println("======================================");
        System.out.println(page.asText());
        System.out.println("======================================");
        System.out.println(page.getWebResponse().getContentAsString());*/


        List<NameValuePair> headers = page.getWebResponse().getResponseHeaders();


        headers.forEach(v -> System.out.println(v.getName() + "=" + v.getValue()));

        Set<Cookie> cookies = webClient.getCookieManager().getCookies();

        System.out.println("=======================");

        cookies.forEach(c -> System.out.println(c.getName() + ":" + c.getValue()));

       /* DomNodeList<DomElement> script = page.getElementsByTagName("script");

        System.out.println(script);

        Object scriptableObject = page.getScriptableObject();

        System.out.println(scriptableObject);

        System.out.println("=========================================");
        System.out.println(page.asText());

        FileCopyUtils.copy(page.getWebResponse().getContentAsStream(), new FileOutputStream("d:/a.txt"));
        System.out.println("=========================================");*/
        JSONObject result = JSONObject.parseObject(webClient.loadWebResponse(ajax[0]).getContentAsString());

        //关掉javascript
        JavaScriptEngine javaScriptEngine = (JavaScriptEngine) webClient.getJavaScriptEngine();

        HtmlUnitContextFactory contextFactory = javaScriptEngine.getContextFactory();

        javaScriptEngine.shutdown();

        String url = result.getString("url");

        webClient.getJavaScriptEngine().shutdown();


        //获取执行后真正的结果
        System.out.println(url);

        page.cleanUp();
        //RI3EhmdfcdR6Nr0CYuMBiXWtzmjPFaSSLmBMjwINSZA
        webClient.close();
    }


    @Test
    public void video() {

    }


    @Test
    public void buttonSubmit() throws Exception {
        //创建webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage page = webClient.getPage("http://www.baidu.com/");
        //获取搜索输入框并提交搜索内容
        HtmlInput input = page.getHtmlElementById("kw");
        System.out.println(input.toString());
        input.setValueAttribute("雅蠛蝶");
        System.out.println(input.toString());
        //获取搜索按钮并点击
        HtmlInput btn = page.getHtmlElementById("su");
        HtmlPage page2 = btn.click();
        //输出新页面的文本
        System.out.println(page2.asText());

        //
    }

    public static void main(String[] args) {


    }
}
