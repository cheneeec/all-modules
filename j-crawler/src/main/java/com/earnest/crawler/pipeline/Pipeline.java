package com.earnest.crawler.pipeline;

import com.earnest.crawler.StringResponseResult;


@FunctionalInterface
public interface Pipeline {
    
    void pipe(StringResponseResult result);

}
