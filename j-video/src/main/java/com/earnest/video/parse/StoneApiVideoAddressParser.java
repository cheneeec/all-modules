package com.earnest.video.parse;

import com.earnest.video.entity.Video;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;

public class StoneApiVideoAddressParser implements VideoAddressParser<Video> {

    private final WebClient webClient;

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
    public String parse(Video video) {
        String playValue = video.getPlayValue();

        return null;
    }
}
