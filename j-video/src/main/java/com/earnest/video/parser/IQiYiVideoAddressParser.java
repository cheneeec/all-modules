package com.earnest.video.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.Browser;
import com.earnest.crawler.proxy.HttpProxyPoolSettingSupport;
import com.earnest.video.exception.ValueParseException;
import com.earnest.video.entity.Episode;
import com.earnest.video.entity.Video;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 爱奇艺的官方解析接口。
 */
@AllArgsConstructor
public class IQiYiVideoAddressParser extends HttpProxyPoolSettingSupport implements VideoAddressParser {

    private final static String SRC = "76f90cbd92f94a2e925d83e8ccd22cb7";
    private final static String KEY = "d5fb4bd9d50c4be6948c97edd7254b0e";

    private final static String API_ADDRESS = "http://cache.m.iqiyi.com/tmts/%s/%s/?t=%s&sc=%s&src=%s";


    private final static Pattern PLAY_INFO_PATTERN = Pattern.compile("video-info='(\\{(.+)url.+\\})'");

    private final HttpClient httpClient;

    private final ResponseHandler<String> responseHandler;
    //清晰度表
    private static final Map<Integer, String> QUALITY_DIC = Map.ofEntries(Map.entry(10, "4k"), Map.entry(19, "4k"), Map.entry(5, "BD"), Map.entry(18, "BD"), Map.entry(21, "HD_H265"), Map.entry(2, "HD"), Map.entry(4, "TD"), Map.entry(17, "TD_H265"), Map.entry(96, "LD"), Map.entry(1, "SD"), Map.entry(14, "TD"));

    private static final Map<String, String> CODEC_DIC = Map.of("4k", "4k", "BD", "1080p", "TD", "720p", "HD", "540p", "SD", "360p", "LD", "210p", "HD_H265", "540p H265", "TD_H265", "720p H265");


    @Override
    public Episode parse(String rawValue, @Nullable Map<String, Object> otherProperties) throws IOException, ValueParseException {
        Episode episode = createEpisode(rawValue, otherProperties);

        Map<String, ?> properties = episode.getProperties();
        //设置请求
        String vid = String.valueOf(properties.get("vid"));
        String tvid = String.valueOf(properties.get("tvid"));
        long t = System.currentTimeMillis() * 1000;
        String sc = DigestUtils.md5DigestAsHex((t + KEY + vid).getBytes());
        HttpUriRequest httpUriRequest = createHttpRequest(String.format(API_ADDRESS, tvid, vid, t, sc, SRC));

        String result = httpClient.execute(httpUriRequest, responseHandler);

        episode.setParseValue(extractParseValue(result));
        return episode;
    }

    @Override
    public boolean support(String rawValue) {
        return rawValue.contains("iqiyi.com");
    }

    @Override
    public int priority() {
        return 5;
    }

    private Episode createEpisode(String playValue, Map<String, Object> otherProperties) throws IOException, ValueParseException {
        if (otherProperties == null || !(otherProperties.containsKey("vid") && otherProperties.containsKey("tvid"))) {
            return createEpisodeByParseHtml(playValue);
        } else {
            Episode episode = new Episode();
            episode.setProperties(otherProperties);
            return episode;
        }
    }

    private Episode createEpisodeByParseHtml(String playValue) throws IOException, ValueParseException {
        Episode episode = new Episode();
        Map<String, String> properties = new HashMap<>();
        episode.setProperties(properties);

        HttpUriRequest httpRequest = createHttpRequest(playValue);
        String result = httpClient.execute(httpRequest, responseHandler);

        Matcher playInfoMatcher = PLAY_INFO_PATTERN.matcher(result);


        if (playInfoMatcher.find()) {
            JSONObject resultJsonObject = JSONObject.parseObject(HtmlUtils.htmlUnescape(playInfoMatcher.group(1)));
            properties.put("tvid", resultJsonObject.getString("tvId"));
            properties.put("vid", resultJsonObject.getString("vid"));
            properties.put("albumId", resultJsonObject.getString("albumId"));

            episode.setShortDescription(resultJsonObject.getString("subtitle"));
            episode.setTitle(resultJsonObject.getString("name"));
            episode.setDescription(resultJsonObject.getString("description"));
            episode.setDuration(resultJsonObject.getIntValue("duration"));
            episode.setImage(resultJsonObject.getString("imageUrl"));

        } else
            throw new ValueParseException("video info is not found");

        return episode;
    }

    private static List<Video.PlayAddress> extractParseValue(String result) throws ValueParseException {
        JSONObject jsonResult = JSONObject.parseObject(result);

        if (!"A00000".equalsIgnoreCase(jsonResult.getString("code"))) {
            throw new ValueParseException("parsing error");
        }

        JSONArray vidl = jsonResult.getJSONObject("data").getJSONArray("vidl");

        return vidl.stream()
                .map(j -> ((JSONObject) j))
                .map(json -> {
                    Video.PlayAddress playAddress = new Video.PlayAddress();
                    String quality = QUALITY_DIC.get(json.getIntValue("vd"));
                    playAddress.setQuality(quality);
                    playAddress.setCodecs(CODEC_DIC.get(quality));
                    playAddress.setType("m3u");
                    playAddress.setUrl(json.getString("m3u"));

                    String[] screenSize = json.getString("screenSize").split("x");
                    if (screenSize.length > 1) {
                        playAddress.setWidth(Integer.parseInt(screenSize[0]));
                        playAddress.setWidth(Integer.parseInt(screenSize[1]));
                    }

                    return playAddress;
                })
                .sorted()
                .collect(Collectors.toList());
    }

    private HttpUriRequest createHttpRequest(String url) {
        RequestBuilder requestBuilder = RequestBuilder.get(url)
                .setHeader(Browser.USER_AGENT, Browser.GOOGLE.userAgent());
        addHttpProxySetting(requestBuilder);
        return requestBuilder
                .build();
    }

}
