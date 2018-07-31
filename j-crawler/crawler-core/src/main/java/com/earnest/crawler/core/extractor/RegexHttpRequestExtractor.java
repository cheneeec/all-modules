package com.earnest.crawler.core.extractor;

import com.earnest.crawler.core.RawTextHttpResponseResult;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Getter
@Setter
public class RegexHttpRequestExtractor extends AbstractHttpRequestExtractor<String,RawTextHttpResponseResult> {
    private final Pattern urlPattern;

    public RegexHttpRequestExtractor(String urlPattern) {
        Assert.hasText(urlPattern, "this String argument:[urlPattern] is empty or null");
        this.urlPattern = Pattern.compile(urlPattern);
    }


    @Override
    protected Set<String> extractUrl(RawTextHttpResponseResult rawTextHttpResponseResult) {
        String content = rawTextHttpResponseResult.getContent();

        Matcher matcher = urlPattern.matcher(content);
        HttpRequest httpRequest =  rawTextHttpResponseResult.getHttpRequest();
        Set<String> newUrls = new HashSet<>();

        String baseUri = StringUtils.replaceFirst(httpRequest.getUrl(), "/*", "");

        while (matcher.find()) {
            String subUrl = matcher.group();
            if (!StringUtils.startsWithAny(subUrl, "http", "https")) {
                //将url进行补全
                subUrl = StringUtil.resolve(baseUri, subUrl);
            }
            //设置跳转
            newUrls.add(subUrl);
        }

        return newUrls;
    }
}
