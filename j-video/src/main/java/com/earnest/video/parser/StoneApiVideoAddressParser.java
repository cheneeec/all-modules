package com.earnest.video.parser;

import com.alibaba.fastjson.JSONObject;
import com.earnest.video.entity.BaseVideoEntity;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.util.Assert;

import java.io.IOException;

public class StoneApiVideoAddressParser implements VideoAddressParser {

    private final WebClient webClient;

    private static final String STONE_API_ADDRESS = "http://jiexi.071811.cc/jx2.php?url=%s";

    public StoneApiVideoAddressParser() {
        webClient = new WebClient(BrowserVersion.CHROME);
        WebClientOptions webClientOptions = webClient.getOptions();
        webClientOptions.setCssEnabled(false);
        webClientOptions.setJavaScriptEnabled(true);//启用JS解释器，默认为true
        webClientOptions.setPrintContentOnFailingStatusCode(false);//在失败的时候打印内容
        webClientOptions.setThrowExceptionOnScriptError(false);//但js执行错误时，是否抛出异常
        webClientOptions.setThrowExceptionOnFailingStatusCode(false);///当HTTP的状态非200时是否抛出异常
        webClientOptions.setActiveXNative(false); //默认false

        webClientOptions.setTimeout(5000);//设置“浏览器”的请求超时时间
        webClient.waitForBackgroundJavaScript(5000);//设置JS后台等待执行时间

    }

    @Override
    public <T extends BaseVideoEntity> BaseVideoEntity parse(T t) throws IOException {

        HtmlPage page = webClient.getPage(t.getPlayValue());

        CachedAjaxController ajaxController = new CachedAjaxController();
        webClient.setAjaxController(ajaxController);

        WebResponse webResponse = webClient.loadWebResponse(ajaxController.obtainResultWebRequest());

        String parseValue = JSONObject.parseObject(webResponse.getContentAsString()).getString("url");

        t.setParseValue(parseValue);

        webResponse.cleanUp();
        page.cleanUp();

        return t;
    }

    private class CachedAjaxController extends AjaxController {
        private WebRequest webRequest;


        @Override
        public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
            this.webRequest = request;
            return super.processSynchron(page, request, async);
        }

        public WebRequest obtainResultWebRequest() {
            Assert.state(webRequest != null, "error status:webRequest is null");

            return webRequest;
        }
    }
}
