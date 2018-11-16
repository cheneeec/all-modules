package com.earnest.video.core.parser;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.Browser;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.httpclient.HtmlUnitRedirectStrategie;
import com.gargoylesoftware.htmlunit.webstart.WebStartHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class StoneApiVideoAddressParserTest {

    StoneApiVideoAddressParser stoneApiVideoAddressParser = new StoneApiVideoAddressParser();

    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    ScriptEngine javascript = scriptEngineManager.getEngineByName("nashorn");

    @Test
    public void parse() throws IOException {
        System.out.println(stoneApiVideoAddressParser.parse("https://www.iqiyi.com/v_19rr5jax4g.html"));
    }

    /**
     * 提交参数：
     * <blockquote>
     * <pre>
     *         {
     *             time:1539100800,
     *             url:
     *             type: iqiyi
     *             pc:0
     *             fuck:
     *
     *           }
     *           time: 1539100800
     *           key: iiJHyalLjFJBzbyNdcUitR9eO-I=
     *           url: https://www.iqiyi.com/v_19rr5jax4g.html
     *           type: iqiyi
     *           pc: 0
     *           fuck: agrIa9k6C6IFMS4NtLw79GwZ5NTsV39yHcmX2AmLtag
     *     </pre>
     *
     * </blockquote>
     *
     * @throws Exception
     */
    @Test
    public void httpClientParse() throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setConnectionRequestTimeout(60 * 1000)
                .build();
        // 1.创建连接
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRedirectStrategy(new HtmlUnitRedirectStrategie())
                .setConnectionTimeToLive(10, TimeUnit.SECONDS)
                .setMaxConnPerRoute(1000000)
                .setRoutePlanner(new DefaultRoutePlanner(null){
                    @Override
                    public HttpRoute determineRoute(HttpHost host, org.apache.http.HttpRequest request, HttpContext context) throws HttpException {
                        System.out.println(host);
                        HttpRoute httpRoute = super.determineRoute(host, request, context);
                        System.out.println(httpRoute);
                        return httpRoute;
                    }
                })
                .build();

        BasicResponseHandler responseHandler = new BasicResponseHandler();
        HttpUriRequest httpGet = RequestBuilder.get("http://jiexi.071811.cc/jx2.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html")
                .addHeader(Browser.USER_AGENT, Browser.GOOGLE.userAgent())
                .build();
        //2. 创建cookie存储

        //3. 创建上下文
        HttpClientContext context = new HttpClientContext();


        String execute = httpClient.execute(httpGet, responseHandler, context);

        //4. 查看内容
        System.out.println("=========cookies=========");
        context.getCookieStore().getCookies().stream()
                .map(c -> c.getName() + ":" + c.getValue())
                .forEach(System.out::println);
        System.out.println("=========headers=========");
        Arrays.stream(context.getResponse().getAllHeaders())
                .map(h -> h.getName() + ":" + h.getValue())
                .forEach(System.out::println);
        System.out.println("=========response=========");


        TimeUnit.SECONDS.sleep(3);
        System.out.println("=========the second request============");  //
        RequestBuilder requestBuilder = RequestBuilder.copy(httpGet)
                .setUri("http://jiexi.071811.cc/stapi.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html")
                .addHeader(Browser.REFERER,"http://jiexi.071811.cc/jx2.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html")
                .addHeader("Host","jiexi.071811.cc");


        Arrays.stream(context.getRequest().getAllHeaders())
                .map(s->s.getName()+";"+s.getValue())
                .forEach(System.out::println);

        Arrays.stream(context.getResponse().getAllHeaders())
                .forEach(h -> requestBuilder.addHeader(h.getName(), h.getValue()));


        System.out.println("=========the second response============");
        CloseableHttpResponse closeableHttpResponse = httpClient.execute(requestBuilder.build(), context);

        System.out.println();


    }

    @Test
    public void seleniumTest() throws Exception {
        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        System.out.println("===============1=================");


        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://jiexi.071811.cc/jx2.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html"))
                .header(Browser.USER_AGENT, Browser.GOOGLE.userAgent())
//                .expectContinue(true)
                .GET().build();

        HttpResponse<String> stringHttpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("=====================request headers=========================");
        stringHttpResponse.request().headers()
                .map()
                .forEach((a,b)-> System.out.println(a+b));
        System.out.println("=====================response headers=========================");
        stringHttpResponse.headers().map()
                .forEach((a,b)-> System.out.println(a+b));


        System.out.println("================2===============");
        HttpRequest httpRequest2 = HttpRequest.newBuilder(URI.create("http://jiexi.071811.cc/stapi.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html"))
                .header(Browser.USER_AGENT, Browser.GOOGLE.userAgent())
                .GET().build();


        HttpResponse<String> stringHttpResponse1 = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
        System.out.println(stringHttpResponse1.body());

    }

    @Test
    public void webClient() throws Exception {
        CookieManager cookieManager = new CookieManager();


        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        WebClientOptions webClientOptions = webClient.getOptions();
        webClientOptions.setCssEnabled(false);
        webClientOptions.setJavaScriptEnabled(false);//启用JS解释器，默认为true
        webClientOptions.setPrintContentOnFailingStatusCode(false);//在失败的时候打印内容
        webClientOptions.setThrowExceptionOnScriptError(false);//但js执行错误时，是否抛出异常
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);///当HTTP的状态非200时是否抛出异常
        webClientOptions.setActiveXNative(false); //默认false

        webClientOptions.setTimeout(5000);//设置“浏览器”的请求超时时间
        webClient.waitForBackgroundJavaScript(5000);//设置JS后台等待执行时间


        webClient.addWebWindowListener(new WebWindowListener() {
            @Override
            public void webWindowOpened(WebWindowEvent event) {
                System.out.println("=============webWindowOpened===========");
                if (event.getNewPage() != null) {
                    System.out.println(event.getNewPage().getUrl());
                }
                if (event.getOldPage() != null) {
                    System.out.println(event.getOldPage().getUrl());
                }

            }

            //"http://jiexi.071811.cc/stapi.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html
            @Override
            public void webWindowContentChanged(WebWindowEvent event) {
                System.out.println("=============webWindowContentChanged===========");
                if (event.getNewPage() != null) {
                    System.out.println(event.getNewPage().getUrl());
                    WebRequest webRequest = event.getNewPage().getWebResponse().getWebRequest();
                    System.out.println(webRequest.getRequestParameters());
                }
                if (event.getOldPage() != null) {
                    System.out.println(event.getOldPage().getUrl());
                }

            }

            @Override
            public void webWindowClosed(WebWindowEvent event) {
                System.out.println("=============webWindowClosed===========");
                if (event.getNewPage() != null) {
                    System.out.println(event.getNewPage().getUrl());
                }
                //http://jiexi.071811.cc/stapi.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html
                if (event.getOldPage() != null) {
                    System.out.println(event.getOldPage().getUrl());
                    DomElement script = ((HtmlPage) event.getOldPage()).getElementsByTagName("script").get(8);
                    String textContent = script.getTextContent();
                    System.out.println(textContent);
                }

            }
        });

       /* final WebRequest[] webRequests = new WebRequest[1];

        webClient.setAjaxController(new AjaxController() {
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
                webRequests[0] = request;
                return super.processSynchron(page, request, async);
            }
        });*/
        List<WebResponse> webResponses = new ArrayList<>();

        webClient.setCookieManager(cookieManager);


        HtmlPage page = webClient.getPage("http://jiexi.071811.cc/jx2.php?url=https://www.iqiyi.com/v_19rr2vbjpo.html");

        System.out.println("============cookies=======================");
        cookieManager.getCookies()
                .stream()
                .map(c -> c.getName() + ":" + c.getValue())
                .forEach(System.out::println);

        System.out.println("============response headers=======================");
        page.getWebResponse().getResponseHeaders()
                .stream()
                .map(h -> h.getName() + ":" + h.getValue())
                .forEach(System.out::println);

        System.out.println("============request headers=======================");
        Map<String, String> headers = page.getWebResponse().getWebRequest().getAdditionalHeaders();
        headers.forEach((key, value) -> System.out.println(key + ":" + value));


      /*  if(webRequests[0]!=null){
            WebResponse webResponse = webClient.loadWebResponse(webRequests[0]);
            String parseValue = JSONObject.parseObject(webResponse.getContentAsString()).getString("url");


            System.out.println("result=>" + parseValue);
            webResponse.cleanUp();
        }*/

        System.out.println(page.getTextContent());


        page.cleanUp();

        System.out.println("==============================");
        httpClient();
    }

    @Test
    public void httpClient() throws IOException {
        CloseableHttpClient aDefault = HttpClients.createDefault();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("time", "1540828800");
        params.put("key", "8xBC7ZU8YXVM1Shiswnwpg6VqiM=");
        params.put("url", "https://www.iqiyi.com/v_19rr2vbjpo.html");
        params.put("type", "iqiyi");
        params.put("pc", "0");
        params.put("fuck", "LR7tNVdSb5aLVP0Or88we8jTnL2gUFc/tYZH7/8xyjA");
        List<BasicNameValuePair> parama1 = params.entrySet()
                .stream()
                .map(e -> new BasicNameValuePair(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parama1);

        RequestBuilder requestBuilder = RequestBuilder.post("http://jiexi.071811.cc/api/xit.php")
                .setEntity(urlEncodedFormEntity);

//        headers.forEach(requestBuilder::addHeader);
        HttpClientContext httpClientContext = new HttpClientContext();
//        CookieStore cookieStore = new BasicCookieStore();
//        cookieManager.getCookies().forEach(cookie -> cookieStore.addCookie(new BasicClientCookie(cookie.getName(), cookie.getValue())));
//        httpClientContext.setCookieStore(cookieStore);

        String execute = aDefault.execute(requestBuilder.build(), new BasicResponseHandler(), httpClientContext);
        System.out.println(execute);
    }

    @Test
    public void js() throws Exception {
        String s = "/*(*^__^*)*/ﾟωﾟﾉ= /｀ｍ´）ﾉ ~┻━┻   //*´∇｀*/ ['_']; o=(ﾟｰﾟ)  =_=3; c=(ﾟΘﾟ) =(ﾟｰﾟ)-(ﾟｰﾟ); (ﾟДﾟ) =(ﾟΘﾟ)= (o^_^o)/ (o^_^o);(ﾟДﾟ)={ﾟΘﾟ: '_' ,ﾟωﾟﾉ : ((ﾟωﾟﾉ==3) +'_') [ﾟΘﾟ] ,ﾟｰﾟﾉ :(ﾟωﾟﾉ+ '_')[o^_^o -(ﾟΘﾟ)] ,ﾟДﾟﾉ:((ﾟｰﾟ==3) +'_')[ﾟｰﾟ] }; (ﾟДﾟ) [ﾟΘﾟ] =((ﾟωﾟﾉ==3) +'_') [c^_^o];(ﾟДﾟ) ['c'] = ((ﾟДﾟ)+'_') [ (ﾟｰﾟ)+(ﾟｰﾟ)-(ﾟΘﾟ) ];(ﾟДﾟ) ['o'] = ((ﾟДﾟ)+'_') [ﾟΘﾟ];(ﾟoﾟ)=(ﾟДﾟ) ['c']+(ﾟДﾟ) ['o']+(ﾟωﾟﾉ +'_')[ﾟΘﾟ]+ ((ﾟωﾟﾉ==3) +'_') [ﾟｰﾟ] + ((ﾟДﾟ) +'_') [(ﾟｰﾟ)+(ﾟｰﾟ)]+ ((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+((ﾟｰﾟ==3) +'_') [(ﾟｰﾟ) - (ﾟΘﾟ)]+(ﾟДﾟ) ['c']+((ﾟДﾟ)+'_') [(ﾟｰﾟ)+(ﾟｰﾟ)]+ (ﾟДﾟ) ['o']+((ﾟｰﾟ==3) +'_') [ﾟΘﾟ];(ﾟДﾟ) ['_'] =(o^_^o) [ﾟoﾟ] [ﾟoﾟ];(ﾟεﾟ)=((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+ (ﾟДﾟ) .ﾟДﾟﾉ+((ﾟДﾟ)+'_') [(ﾟｰﾟ) + (ﾟｰﾟ)]+((ﾟｰﾟ==3) +'_') [o^_^o -ﾟΘﾟ]+((ﾟｰﾟ==3) +'_') [ﾟΘﾟ]+ (ﾟωﾟﾉ +'_') [ﾟΘﾟ]; (ﾟｰﾟ)+=(ﾟΘﾟ); (ﾟДﾟ)[ﾟεﾟ]='\\\\'; (ﾟДﾟ).ﾟΘﾟﾉ=(ﾟДﾟ+ ﾟｰﾟ)[o^_^o -(ﾟΘﾟ)];(oﾟｰﾟo)=(ﾟωﾟﾉ +'_')[c^_^o];(ﾟДﾟ) [ﾟoﾟ]='\\\"';(ﾟДﾟ) ['_'] ( (ﾟДﾟ) ['_'] (ﾟεﾟ+/*´∇｀*/(ﾟДﾟ)[ﾟoﾟ]+ (ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) - (ﾟΘﾟ))+((ﾟｰﾟ) + (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(c^_^o)+((o^_^o) +(o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟｰﾟ)+(o^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (ﾟΘﾟ))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((o^_^o) +(o^_^o))+(ﾟｰﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(o^_^o)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(o^_^o)+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(ﾟΘﾟ)+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+((o^_^o) +(o^_^o))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (ﾟΘﾟ))+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (o^_^o))+(c^_^o)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+((ﾟｰﾟ) + (ﾟΘﾟ))+((o^_^o) - (ﾟΘﾟ))+(ﾟДﾟ)[ﾟεﾟ]+(ﾟΘﾟ)+(c^_^o)+(ﾟΘﾟ)+(ﾟДﾟ)[ﾟεﾟ]+(ﾟｰﾟ)+((ﾟｰﾟ) + (o^_^o))+(ﾟДﾟ)[ﾟεﾟ]+((ﾟｰﾟ) + (o^_^o))+(o^_^o)+(ﾟДﾟ)[ﾟoﾟ]) (ﾟΘﾟ)) ('_');";
        javascript.eval(s);
        Assert.assertSame(javascript.get("fuck"), "LR7tNVdSb5aLVP0Or88we8jTnL2gUFc/tYZH7/8xyjA");
    }

    @Test
    public void reg() throws Exception {
        Resource resource = new ClassPathResource("example.js");
        String s = FileUtils.readFileToString(resource.getFile(), Charset.defaultCharset());
        Pattern pattern = Pattern.compile("\"api/xit.php\", .*(\\{.+fuck})");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            String group = matcher.group(1);
            System.out.println(group);
            JSONObject jsonObject = JSONObject.parseObject(group);
            System.out.println(jsonObject.getString("fuck"));


//            javascript.eval(matcher.group(1));
//            System.out.println(javascript.get("fuck"));
        }

    }

    @Test
    public void aa() {
        String s = "{\"time\":\"1540828800\", \"key\": \"8xBC7ZU8YXVM1Shiswnwpg6VqiM=\", \"url\": \"https://www.iqiyi.com/v_19rr2vbjpo.html\",\"type\": \"iqiyi\",\"pc\": \"0\",'fuck':'1'}";
        System.out.println(JSONObject.parseObject(s).getInnerMap());
    }
}