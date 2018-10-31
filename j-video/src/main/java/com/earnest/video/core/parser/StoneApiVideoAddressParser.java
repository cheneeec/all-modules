package com.earnest.video.core.parser;

import com.alibaba.fastjson.JSONObject;
import com.earnest.video.core.ValueParseException;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
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

public class StoneApiVideoAddressParser implements VideoAddressParser {

    private final WebClient webClient;

    private static final String STONE_API_ADDRESS = "http://jiexi.071811.cc/jx2.php?url=%s";

    private final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

    private final HttpClient httpClient;

    //颜文字的正则表达式
    private final Pattern aaPattern = Pattern.compile("(\\/\\*\\(\\*\\^__\\^\\*\\)\\*\\/ﾟωﾟﾉ= /｀ｍ´）ﾉ ~┻━┻   //\\*´∇｀\\*/\\ \\['_'];.+;).*function ");
    //提交参数的正则表达式
    private final Pattern paramsPattern = Pattern.compile("\"api/xit.php\", .*(\\{.+fuck})");

    private final ThreadLocal<String> requestParamsScriptString = new ThreadLocal<>();


    public StoneApiVideoAddressParser(HttpClient httpClient) {
        Assert.notNull(httpClient, "httpClient is required");
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
            public void webWindowClosed(WebWindowEvent event) {
                Page page = event.getOldPage();
                if (page != null && StringUtils.contains(page.getUrl().toString(), "http://jiexi.071811.cc/stapi.php")) {
                    requestParamsScriptString.set(((HtmlPage) event.getOldPage()).getElementsByTagName("script").get(8).getTextContent());
                }
            }
        });

        this.httpClient = httpClient;
    }

    public StoneApiVideoAddressParser() {
        this(HttpClients.createDefault());
    }

    //TODO 待优化
    @Override
    public String parse(String playValue) throws IOException {

        HtmlPage page = webClient.getPage(String.format(STONE_API_ADDRESS, playValue));


//        WebResponse webResponse = webClient.loadWebResponse(ajaxController.obtainResultWebRequest());


//        String parseValue = JSONObject.parseObject(webResponse.getContentAsString()).getString("url");


        String requestParamsScriptString = this.requestParamsScriptString.get();


        Matcher aaMatcher = aaPattern.matcher(requestParamsScriptString);
        Matcher paramsMatcher = paramsPattern.matcher(requestParamsScriptString);

        if (!(aaMatcher.find() && paramsMatcher.find())) {
            throw new ValueParseException("parse failed");
        }

        UrlEncodedFormEntity urlEncodedFormEntity = buildRequestParams(aaMatcher, paramsMatcher, scriptEngine);


//        webResponse.cleanUp();
        page.cleanUp();

        return null;
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
            String params = paramsMatcher.group(1);
            //替换最后一个参数
            String requestParams = StringUtils.replaceAll(params, "fuck", fuck).replaceFirst(fuck, "fuck");

            List<BasicNameValuePair> nameValuePairs = JSONObject.parseObject(requestParams).getInnerMap()
                    .entrySet()
                    .stream()
                    .map(r -> new BasicNameValuePair(r.getKey(), r.getValue().toString()))
                    .collect(Collectors.toList());

            return new UrlEncodedFormEntity(nameValuePairs);


        } catch (ScriptException e) {
            throw new ValueParseException(e.getMessage(), e);
        }
    }

    private class CachedAjaxController extends AjaxController {

        private WebRequest webRequest;


        @Override
        public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
            this.webRequest = request;
            return super.processSynchron(page, request, async);
        }

        WebRequest obtainResultWebRequest() {
            Assert.state(webRequest != null, "error status:webRequest is null");
            return webRequest;
        }
    }

}
