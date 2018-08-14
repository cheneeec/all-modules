package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.StringResponseResult;

@FunctionalInterface
public interface FileNameGenerator {

    String generate(StringResponseResult result);
}
