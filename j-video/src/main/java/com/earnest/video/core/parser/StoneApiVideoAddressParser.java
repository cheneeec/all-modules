package com.earnest.video.core.parser;

import com.alibaba.fastjson.JSONObject;
import com.earnest.video.core.ValueParseException;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
@CacheConfig(cacheNames ="stone" )
public class StoneApiVideoAddressParser implements VideoAddressParser {

    private final WebClient webClient;

    private static final String STONE_API_ADDRESS = "http://jiexi.071811.cc/jx2.php?url=%s";

    private final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

    private final HttpClient httpClient;

    private final ResponseHandler<String> responseHandler;


    //颜文字的正则表达式
    private final Pattern aaPattern = Pattern.compile("(\\/\\*\\(\\*\\^__\\^\\*\\)\\*\\/ﾟωﾟﾉ= /｀ｍ´）ﾉ ~┻━┻   //\\*´∇｀\\*/\\ \\['_'];.+;).*function ");
    //提交参数的正则表达式
    private final Pattern paramsPattern = Pattern.compile("\"api/xit.php\", .*(\\{.+fuck})");

    private final ThreadLocal<String> requestParamsScriptString = new ThreadLocal<>();


    public StoneApiVideoAddressParser(HttpClient httpClient, ResponseHandler<String> responseHandler) {
        Assert.notNull(httpClient, "httpClient is required");
        Assert.notNull(responseHandler, "responseHandler is required");
        webClient = new WebClient(BrowserVersion.CHROME);
        WebClientOptions webClientOptions = webClient.getOptions();
        webClientOptions.setCssEnabled(false);
        webClientOptions.setJavaScriptEnabled(false);//启用JS解释器
        webClientOptions.setPrintContentOnFailingStatusCode(false);//在失败的时候打印内容
        webClientOptions.setThrowExceptionOnScriptError(false);//但js执行错误时，是否抛出异常
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);///当HTTP的状态非200时是否抛出异常
        webClientOptions.setActiveXNative(false); //默认false
        webClientOptions.setTimeout(5000);//设置“浏览器”的请求超时时间
        webClient.waitForBackgroundJavaScript(5000);//设置JS后台等待执行时间

        webClient.addWebWindowListener(new WebWindowAdapter() {
            @Override
            public void webWindowContentChanged(WebWindowEvent event) {
                Page newPage = event.getNewPage();
                if (newPage != null && StringUtils.contains(newPage.getUrl().toString(), "http://jiexi.071811.cc/stapi.php")) {
                    requestParamsScriptString.set(((HtmlPage) newPage).getElementsByTagName("script").get(6).getTextContent());
                }
            }
        });

        this.httpClient = httpClient;
        this.responseHandler = responseHandler;

    }

    public StoneApiVideoAddressParser(ResponseHandler<String> responseHandler) {
        this(HttpClients.createDefault(), responseHandler);

    }

    public StoneApiVideoAddressParser() {
        this(HttpClients.createDefault(), new BasicResponseHandler());
    }

    //TODO 待优化
    @Cacheable()
    @Override
    public List<String> parse(String playValue) throws IOException {

        HtmlPage page = webClient.getPage(String.format(STONE_API_ADDRESS, playValue));


        String requestParamsScriptString = this.requestParamsScriptString.get();


        Matcher aaMatcher = aaPattern.matcher(requestParamsScriptString);
        Matcher paramsMatcher = paramsPattern.matcher(requestParamsScriptString);

        if (!(aaMatcher.find() && paramsMatcher.find())) {
            throw new ValueParseException("parse failed");
        }

        WebResponse webResponse = page.getWebResponse();

        HttpUriRequest httpUriRequest = createHttpUriRequest(webResponse, buildRequestParams(aaMatcher, paramsMatcher, scriptEngine));


        String result = httpClient.execute(httpUriRequest, responseHandler);

        if (StringUtils.isBlank(result)) {
            throw new ValueParseException("parse failed");
        }

        webResponse.cleanUp();
        page.cleanUp();
        this.requestParamsScriptString.remove();

        return List.of(JSONObject.parseObject(result).getString("url"));
    }

    /**
     * 创建{@link HttpUriRequest}。
     *
     * @param webResponse
     * @param urlEncodedFormEntity
     * @return
     */
    private static HttpUriRequest createHttpUriRequest(WebResponse webResponse, UrlEncodedFormEntity urlEncodedFormEntity) {
        //组装请求
        RequestBuilder requestBuilder = RequestBuilder.post("http://jiexi.071811.cc/api/xit.php");

        //添加请求头
        webResponse.getWebRequest().getAdditionalHeaders()
                .forEach(requestBuilder::addHeader);


        return requestBuilder.setEntity(urlEncodedFormEntity).build();
    }

    /**
     * 从请求的<code>http://jiexi.071811.cc/stapi.php</code>中获取请求参数。
     *
     * @param aaMatcher
     * @param paramsMatcher
     * @param scriptEngine
     * @return
     * @throws UnsupportedEncodingException
     * @throws ValueParseException
     */
    private static UrlEncodedFormEntity buildRequestParams(Matcher aaMatcher, Matcher paramsMatcher, ScriptEngine scriptEngine) throws ValueParseException, UnsupportedEncodingException {
        //执行
        try {
            //获取fuck值
            scriptEngine.eval(aaMatcher.group(1));
            String fuck = (String) scriptEngine.get("fuck");
            //获取提交参数
            String params = RegExUtils.removeAll(paramsMatcher.group(1), "\'");
            //替换最后一个参数

            String requestParams = RegExUtils.replaceAll(params, "fuck", "\"" + fuck + "\"")
                    .replaceFirst(fuck, "fuck");

            //获取提交内容参数
            List<BasicNameValuePair> requestParamsBody = JSONObject.parseObject(requestParams).getInnerMap()
                    .entrySet()
                    .stream()
                    .map(r -> new BasicNameValuePair(r.getKey(), r.getValue().toString()))
                    .collect(Collectors.toList());


            return new UrlEncodedFormEntity(requestParamsBody);

        } catch (ScriptException e) {
            throw new ValueParseException(e.getMessage(), e);
        }
    }


}
