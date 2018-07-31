package com.earnest.core.downloader;

import com.earnest.crawler.core.builder.SpiderBuilder;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

public class Pipeline {

    @Test
    public void tt() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        System.out.println(pathMatcher.match("**", "https://translate.google.cn/#zh-CN/en/from%E6%B2%A1%E6%9C%89%E8%AE%BE%E7%BD%AE"));

        SpiderBuilder spiderBuilder=new SpiderBuilder();

    }

}
