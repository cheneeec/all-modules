package com.earnest.video.parser;

import com.earnest.crawler.Browser;
import com.earnest.crawler.proxy.HttpProxyPoolSettingSupport;
import com.earnest.video.entity.Episode;
import com.earnest.video.entity.Video;
import com.earnest.video.exception.ValueParseException;
import lombok.AllArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * 使用<a href="https://2wk.com/api/qiyi.php">爱悟空</a>的<code>API</code>接口进行解析。
 */
@AllArgsConstructor
public class AwkVideoAddressParser extends HttpProxyPoolSettingSupport implements VideoAddressParser {

    //    private static final String API_ADDRESS = "https://2wk.com/api/qiyi.php?url=https://www.iqiyi.com/v_19rr3rqkqk.html";
    private static final String API_ADDRESS = "https://2wk.com/api/qiyi.php?url=%s";

    private final HttpClient httpClient;
    private final ResponseHandler<String> responseHandler;

    private static final Pattern SCRIPT_PATTERN = Pattern.compile("<script>(\\n{0,}.+\\n{0,}.+\\n{0,})</script>");

    private static final Pattern URL_PATTERN = Pattern.compile("<video\\s+src=\"(.+)\"\\s+controls");

    @Override
    public Episode parse(String rawValue, Map<String, Object> properties) throws IOException, ValueParseException {
        String responseString = httpClient.execute(createHttpUriRequest(rawValue), responseHandler);
        Matcher scriptMatcher = SCRIPT_PATTERN.matcher(responseString);
        String resultScript = "";
        if (scriptMatcher.find()) {
            resultScript += scriptMatcher.group(1);

        }
        Matcher urlMatcher = URL_PATTERN.matcher(responseString);
        if (urlMatcher.find()) {
            resultScript += urlMatcher.group(1);
        }

        Video.PlayAddress playAddress = new Video.PlayAddress();
        playAddress.setScript(resultScript);

        Episode episode = new Episode();
        episode.setParseValue(List.of(playAddress));

        return episode;
    }

    private static HttpUriRequest createHttpUriRequest(String url) {
        return RequestBuilder.get(format(API_ADDRESS, url)).addHeader(Browser.USER_AGENT, Browser.GOOGLE.userAgent()).build();
    }

    @Override
    public int priority() {
        return 1;
    }


}
