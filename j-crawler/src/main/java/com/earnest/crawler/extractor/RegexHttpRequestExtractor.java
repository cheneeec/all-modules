package com.earnest.crawler.extractor;

import com.earnest.crawler.HttpResponseResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.helper.StringUtil;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class RegexHttpRequestExtractor extends AbstractHttpRequestExtractor {

    private final Pattern urlPattern;

    public RegexHttpRequestExtractor(String urlPattern) {
        Assert.hasText(urlPattern, "this String argument:[urlPattern] is empty or null");
        this.urlPattern = Pattern.compile(urlPattern);
    }


    @Override
    protected Set<String> extractUrl(HttpResponseResult<String> responseResult) {

        String content = responseResult.getContent();

        Matcher matcher = urlPattern.matcher(content);
        HttpUriRequest httpRequest = responseResult.getHttpRequest();
        Set<String> newUrls = new HashSet<>();

        String baseUri = RegExUtils.replaceFirst(httpRequest.getURI().toString(), "/*", "");

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
