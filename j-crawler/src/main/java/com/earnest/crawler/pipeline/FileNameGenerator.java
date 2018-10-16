package com.earnest.crawler.pipeline;

import com.earnest.crawler.StringResponseResult;

@FunctionalInterface
public interface FileNameGenerator {

    String generate(StringResponseResult result);
}
