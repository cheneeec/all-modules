package com.earnest.crawler.core.pipeline;

import com.earnest.crawler.core.StringResponseResult;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;

public class UriPathFileNameGenerator implements FileNameGenerator {

    public final static UriPathFileNameGenerator INSTANCE = new UriPathFileNameGenerator();

    private UriPathFileNameGenerator() {}


    @Override
    public String generate(StringResponseResult result) {
        URI uri = result.getHttpRequest().getURI();
        return StringUtils.replaceAll(uri.getHost() + uri.getPath(), "\\/", "_");
    }



}
