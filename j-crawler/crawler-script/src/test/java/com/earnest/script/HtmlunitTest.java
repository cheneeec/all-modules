package com.earnest.script;

import com.earnest.crawler.core.Browser;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HtmlunitTest {

    private final CookieStore cookieStore = new BasicCookieStore();
    private final HttpClient httpClient = HttpClients.custom()
            .setUserAgent(Browser.GOOGLE.userAgent())
            .setDefaultCookieStore(cookieStore)
            .build();

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

       /* final WebRequest[] ajax = new WebRequest[1];


        webClient.setAjaxController(new AjaxController() {
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
                System.out.println(request.getUrl());
                System.out.println(request.getRequestBody());
                System.out.println(request.getHttpMethod());
                ajax[0] = request;
                return super.processSynchron(page, request, async);
            }
        });*/


        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(WebWindowEvent event) {
            }

            @Override
            public void webWindowContentChanged(WebWindowEvent event) {

                Page newPage = event.getNewPage();
                System.out.println(newPage.getUrl());
                if (!"http://jiexi.071811.cc/stapi.php?url=https://www.iqiyi.com/v_19rr5jax4g.html".equals(newPage.getUrl().toString())) {
                    JavaScriptJobManager jobManager = event.getWebWindow().getJobManager();
                    jobManager.removeAllJobs();
                    WebResponse webResponse = newPage.getWebResponse();
                    ((HtmlPage) newPage).getElementsByTagName("script").stream()
                            .map(DomNode::getTextContent)
                            .filter(s -> StringUtils.contains(s, "hlsjsConfig"))
                            .findAny()
                            .ifPresent(s -> {
                                ScriptResult scriptResult = ((HtmlPage) newPage).executeJavaScript("/*(*^__^*)*/ﾟωﾟﾉ= /｀ｍ´）ﾉ ~┻━┻   //*´∇｀*/ ['_']; o=(ﾟｰﾟ)  =_=3; c=(ﾟΘﾟ) =(ﾟｰﾟ)-(ﾟｰﾟ); (ﾟДﾟ) =(ﾟΘﾟ)= (o^_^o)/ (o^_^o);(ﾟДﾟ)={ﾟΘﾟ: '_' ,ﾟωﾟﾉ : ((ﾟωﾟﾉ==3) +'_') [ﾟΘﾟ] ,ﾟｰﾟﾉ :(ﾟωﾟﾉ+ '_')[o^_^o -(ﾟΘﾟ)] ,ﾟДﾟﾉ:((ﾟｰﾟ==3) +'_')[ﾟｰﾟ] }; (ﾟДﾟ) [ﾟΘﾟ] =((ﾟωﾟﾉ==3) +'_') [c^_^o];(ﾟДﾟ) ['c'] = ((ﾟДﾟ)+'_') [ (ﾟｰﾟ)+(ﾟｰﾟ)-(ﾟΘﾟ) ];(ﾟДﾟ) ['o'] = ((ﾟДﾟ)+'_') [ﾟΘﾟ];(ﾟoﾟ)=(ﾟДﾟ) ['c']+(ﾟДﾟ) ['o']+(ﾟωﾟﾉ +'_')[ﾟΘﾟ]+ ((ﾟωﾟﾉ==3) +'_') [ﾟｰﾟ] + ((ﾟДﾟ) +'_') [(ﾟｰﾟ)+(ﾟｰﾟ)]+ ((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+((ﾟｰﾟ==3) +'_') [(ﾟｰﾟ) - (ﾟΘﾟ)]+(ﾟДﾟ) ['c']+((ﾟДﾟ)+'_') [(ﾟｰﾟ)+(ﾟｰﾟ)]+ (ﾟДﾟ) ['o']+((ﾟｰﾟ==3) +'_') [ﾟΘﾟ];(ﾟДﾟ) ['_'] =(o^_^o) [ﾟoﾟ] [ﾟoﾟ];(ﾟεﾟ)=((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+ (ﾟДﾟ) .ﾟДﾟﾉ+((ﾟДﾟ)+'_') [(ﾟｰﾟ) + (ﾟｰﾟ)]+((ﾟｰﾟ==3) +'_') [o^_^o -ﾟΘﾟ]+((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+ (ﾟωﾟﾉ +'_') [ﾟΘﾟ]; (ﾟｰﾟ)+=(ﾟΘﾟ); (ﾟДﾟ)[ﾟεﾟ]='\\\\'; (ﾟДﾟ).ﾟΘﾟﾉ=(ﾟДﾟ+ ﾟｰﾟ)[o^_^o -(ﾟΘﾟ)];(oﾟｰﾟo)=(ﾟωﾟﾉ +'_')[c^_^o];(ﾟДﾟ) [ﾟoﾟ]='\\\"';(ﾟДﾟ) ['_'] ( (ﾟДﾟ) ['_'] (ﾟεﾟ+/*´∇｀*/(ﾟДﾟ)[ﾟoﾟ]+ (ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(c^_^o)+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(c^_^o)+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(c^_^o)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(o^_^o)+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(o^_^o)+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(c^_^o)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(o^_^o)+(ﾟДﾟ)[ﾟoﾟ]) (ﾟΘﾟ)) ('_');");
                                Object result = scriptResult.getJavaScriptResult();
                                Page newPage1 = scriptResult.getNewPage();
                                System.out.println(result);
                                System.out.println(newPage1.getWebResponse().getContentAsString());
                            });

                    HTMLDocument scriptableObject = ((HtmlPage) newPage).getScriptableObject();


                }


                if ("http://jiexi.071811.cc/api/xit.php".equalsIgnoreCase(newPage.getUrl().toString())) {
                    System.out.println("content=>" + newPage.getWebResponse().getContentAsString());
                }

                // http://jiexi.071811.cc/api/xit.php

                if (event.getOldPage() != null) {
                    System.out.println("=>" + event.getOldPage().getUrl());
                }

            }

            @Override
            public void webWindowClosed(WebWindowEvent event) {

            }
        });

        HtmlPage page = webClient.getPage("http://jiexi.071811.cc/jx2.php?url=https://www.iqiyi.com/v_19rr5jax4g.html");
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
//        JSONObject result = JSONObject.parseObject(webClient.loadWebResponse(ajax[0]).getContentAsString());

        //关掉javascript
        JavaScriptEngine javaScriptEngine = (JavaScriptEngine) webClient.getJavaScriptEngine();


        javaScriptEngine.shutdown();

//        String url = result.getString("url");


        //获取执行后真正的结果
//        System.out.println(url);

        webClient.getJavaScriptEngine().shutdown();


        page.cleanUp();
        //RI3EhmdfcdR6Nr0CYuMBiXWtzmjPFaSSLmBMjwINSZA
        webClient.close();


        RequestBuilder requestBuilder = RequestBuilder.post("http://jiexi.071811.cc/api/xit.php");



        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("time", "1539187200"));
        params.add(new BasicNameValuePair("key", "iiJHyalLjFJBzbyNdcUitR9eO-I="));
        params.add(new BasicNameValuePair("url", "https://www.iqiyi.com/v_19rr5jax4g.html"));
        params.add(new BasicNameValuePair("type", "iqiyi"));
        params.add(new BasicNameValuePair("pc", "0"));
        params.add(new BasicNameValuePair("fuck", "agrIa9k6C6IFMS4NtLw79GwZ5NTsV39yHcmX2AmLtag"));

        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params);

        cookies.stream()
                .map(c->new BasicClientCookie(c.getName(),c.getValue()))
                .forEach(cookieStore::addCookie);




        headers.
                stream()
                .filter(s -> !StringUtils.equalsAnyIgnoreCase(s.getName(), "Transfer-encoding"))
                .forEach(h -> requestBuilder.addHeader(h.getName(), h.getValue()));


        requestBuilder.setEntity(urlEncodedFormEntity);
        HttpUriRequest build = requestBuilder.build();
        System.out.println(build);
        String execute = httpClient.execute(build, new BasicResponseHandler());
        System.out.println(execute);


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

}
