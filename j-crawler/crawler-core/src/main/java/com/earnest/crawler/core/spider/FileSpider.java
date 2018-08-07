package com.earnest.crawler.core.spider;

import com.earnest.crawler.core.HttpResponseResult;
import org.springframework.util.FileCopyUtils;

import java.io.FileWriter;
import java.io.IOException;


public class FileSpider extends AbstractSpider {

    private final String path;

    public FileSpider(String path) {
        this.path = path;
    }

    @Override
    protected void handle(HttpResponseResult<String> stringHttpResponseResult) throws IOException {
        String fileName = System.currentTimeMillis()+".txt";



        FileCopyUtils.copy(stringHttpResponseResult.getContent(), new FileWriter(path + fileName));
    }
}
