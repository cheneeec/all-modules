package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.StringResponseResult;


@FunctionalInterface
public interface Pipeline {
    
    void pipe(StringResponseResult result);

}
