package com.earnest.crawler.core.builder;

import com.earnest.crawler.core.extractor.CssSelectorHttpRequestExtractor;
import com.earnest.crawler.core.extractor.EmptyHttpRequestExtractor;
import com.earnest.crawler.core.extractor.HttpRequestExtractor;
import com.earnest.crawler.core.extractor.RegexHttpRequestExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.nodes.Document;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class HttpUriRequestExtractorConfigurer extends SharedSpiderConfigurer<HttpRequestExtractor> {


    private HttpRequestExtractor requestExtractor;
    private final Pattern pattern = Pattern.compile("\\$\\{(\\d+)~(\\d+)\\}");
    private final int[] ranges = new int[2];

    private String uriTemplate;

    /**
     * 使用正则表达式提取新的链接。
     *
     * @param pattern
     * @return
     */
    public HttpUriRequestExtractorConfigurer match(String pattern) {
        this.requestExtractor = new RegexHttpRequestExtractor(pattern);
        return this;
    }

    /**
     * 使用css选择器提取新的链接。
     *
     * @param cssSelectorExtractor
     * @return
     */
    public HttpUriRequestExtractorConfigurer select(Function<Document, Set<String>> cssSelectorExtractor) {
        this.requestExtractor = new CssSelectorHttpRequestExtractor(cssSelectorExtractor);
        return this;
    }

    /**
     * 选取一个固定的范围获得链接。
     * <blockquote>
     * 例:
     * <pre>
     *         <code>http://list.iqiyi.com/www/4/38-------------4-${1~30}-1-iqiyi--.html</code>
     * </pre>
     * </blockquote>
     * 抓取从1到30页的内容。
     *
     * @return
     */
    public HttpUriRequestExtractorConfigurer range(String rangeRegexUrl) {
        Matcher matcher = pattern.matcher(rangeRegexUrl);
        Assert.isTrue(matcher.find(), "rangeRegexUrl is incorrect,should contain content in the form ${start~end}");
        ranges[0] = Integer.parseInt(matcher.group(1));
        ranges[1] = Integer.parseInt(matcher.group(2));
        uriTemplate = rangeRegexUrl;
        this.requestExtractor = new EmptyHttpRequestExtractor();
        return this;
    }


    @Override
    protected int order() {
        return 3;
    }

    @Override
    void configure() {

        if (!ArrayUtils.isEmpty(ranges) && StringUtils.isNotBlank(uriTemplate)) {
            @SuppressWarnings("unchecked")
            List<HttpUriRequest> httpUriRequests = (List<HttpUriRequest>) sharedObjectMap.get(HttpUriRequest.class);
            //获得模板的请求
            HttpUriRequest httpUriRequest = httpUriRequests.get(0);

            Set<String> uris = httpUriRequests.stream()
                    .map(HttpUriRequest::getRequestLine)
                    .map(RequestLine::getUri)
                    .collect(Collectors.toSet());


            IntStream.range(ranges[0], ranges[1] + 1)
                    .mapToObj(i -> StringUtils.replacePattern(uriTemplate, pattern.pattern(), String.valueOf(i)))
                    .filter(uri -> !uris.contains(uri))
                    .map(u ->
                            (httpUriRequest == null ? RequestBuilder.get(u) : RequestBuilder.copy(httpUriRequest).setUri(u)).build()
                    )
                    .peek(h -> log.trace("Generated a new Url:{}", h.getURI()))
                    .forEach(httpUriRequests::add);

        }

        sharedObjectMap.put(HttpRequestExtractor.class, Collections.singletonList(requestExtractor));
    }

}
