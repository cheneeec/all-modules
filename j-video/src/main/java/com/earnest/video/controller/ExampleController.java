package com.earnest.video.controller;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.video.spider.IQiYiSpider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    private final Spider spider = new IQiYiSpider();

    @GetMapping("/start")
    public void start() {
        spider.start();
    }

    @GetMapping("/stop")
    public void stop() {
        spider.shutdown();
    }
}
