package com.earnest.crawler.core.handler;

import com.earnest.crawler.core.request.AbstractHttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jsoup.helper.StringUtil;
import org.springframework.util.Assert;


import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class RegexHttpResponseHandler extends AbstractHttpResponseHandler {

    private final Pattern urlPattern;

    public RegexHttpResponseHandler(String urlPattern) {
        Assert.hasText(urlPattern, "this String argument:[urlPattern] is empty or null");
        this.urlPattern = Pattern.compile(urlPattern);
    }

    @Override
    protected Set<String> extract(HttpResponse httpResponse) {
        String content = httpResponse.getContent();
        Matcher matcher = urlPattern.matcher(content);
        AbstractHttpRequest httpRequest = (AbstractHttpRequest) httpResponse.getHttpRequest();
        Set<String> newUrls = new HashSet<>();
        //
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RegexHttpResponseHandler that = (RegexHttpResponseHandler) o;

        return new EqualsBuilder()
                .append(urlPattern.pattern(), that.urlPattern.pattern())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(urlPattern.pattern())
                .toHashCode();
    }
}
