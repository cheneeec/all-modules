package com.earnest.script;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.Test;

public class HtmlunitTest {

    @Test
    public void show() throws Exception{
        WebClient webClient=new WebClient();
        //禁用CSS
        webClient.getOptions().setCssEnabled(false);
        //禁用javascript支持
        webClient.getOptions().setJavaScriptEnabled(false);
        HtmlPage page = webClient.getPage("https://www.baidu.com/s?wd=%E5%90%BE%E7%88%B1%E7%A0%B4%E8%A7%A3&rsv_spt=1&rsv_iqid=0xe4ef7f6c00006ac5&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&rqlang=cn&tn=baiduhome_pg&rsv_enter=1&inputT=2544&rsv_t=01f28aInT%2Fb74s4QXVVZpXfKkdYvzMvJTCz1shi6mIGoLTCELdPqrH2WVdpRfprL01l8&oq=%25E7%25B3%25BB%25E7%25BB%259F%25E8%25AE%25BE%25E8%25AE%25A1&rsv_pq=a6d76f1000007a44&rsv_sug3=24&rsv_sug1=14&rsv_sug7=100&rsv_sug2=0&rsv_sug4=2544");

        System.out.println(page.asText());
        webClient.close();
    }

    @Test
    public void buttonSubmit() throws Exception{
        //创建webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage page = webClient.getPage("http://www.baidu.com/");
        //获取搜索输入框并提交搜索内容
        HtmlInput input =page.getHtmlElementById("kw");
        System.out.println(input.toString());
        input.setValueAttribute("雅蠛蝶");
        System.out.println(input.toString());
        //获取搜索按钮并点击
        HtmlInput btn = page.getHtmlElementById("su");
        HtmlPage page2 = btn.click();
        //输出新页面的文本
        System.out.println(page2.asText());
    }
}
