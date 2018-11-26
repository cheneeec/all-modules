package com.earnest.crawler.builder;

import com.earnest.crawler.Browser;
import com.earnest.crawler.extractor.CssSelectorHttpRequestExtractor;
import com.earnest.crawler.extractor.EmptyHttpRequestExtractor;
import com.earnest.crawler.extractor.HttpRequestExtractor;
import com.earnest.crawler.extractor.RegexHttpRequestExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.jsoup.nodes.Document;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Slf4j
public class HttpUriRequestExtractorConfigurer extends SharedSpiderConfigurer {


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
    public SharedSpiderConfigurer match(String pattern) {
        this.requestExtractor = new RegexHttpRequestExtractor(pattern);
        return this;
    }

    /**
     * 使用css选择器提取新的链接。
     *
     * @param cssSelectorExtractor
     * @return
     */
    public SharedSpiderConfigurer select(Function<Document, Set<String>> cssSelectorExtractor) {
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
    public SharedSpiderConfigurer range(String rangeRegexUrl) {
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
            //
            @SuppressWarnings("unchecked")
            List<HttpUriRequest> httpUriRequests = (List<HttpUriRequest>) sharedObjectMap.get(HttpUriRequest.class);
            //获得模板的请求
            HttpUriRequest httpUriRequestTemplate = httpUriRequests.get(0);



            IntStream.range(ranges[0], ranges[1] + 1)
                    .mapToObj(page ->
                            new String[]{RegExUtils.replacePattern(uriTemplate, pattern.pattern(), String.valueOf(page)), String.valueOf(page)}
                    )
                    .filter(uriArray ->
                            httpUriRequests.stream().map(HttpUriRequest::getRequestLine)
                                    .map(RequestLine::getUri)
                                    .anyMatch(s -> !uriArray[0].equalsIgnoreCase(s))
                    )
                    .map(u -> {
                                Optional<HttpUriRequest> httpUriRequestOptional = Optional.ofNullable(httpUriRequestTemplate);
                                RequestBuilder requestBuilder = httpUriRequestOptional.map(RequestBuilder::copy).orElseGet(RequestBuilder::get);
                                requestBuilder.setUri(u[0]);
                                if (Integer.valueOf(u[1]) != ranges[0]) {
                                    requestBuilder.setHeader(Browser.REFERER, RegExUtils.replacePattern(uriTemplate, pattern.pattern(), String.valueOf(Integer.valueOf(u[1]) - 1)));
                                }
                                return requestBuilder.build();
                            }
                    )
                    .peek(h -> log.trace("Generated a new Url:{}", h.getURI()))
                    .forEach(httpUriRequests::add);

        }

        sharedObjectMap.put(HttpRequestExtractor.class, Collections.singletonList(requestExtractor));
    }

}
