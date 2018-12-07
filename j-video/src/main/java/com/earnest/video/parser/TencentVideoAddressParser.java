package com.earnest.video.parser;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.Browser;
import com.earnest.crawler.proxy.HttpProxyPoolSettingSupport;
import com.earnest.video.entity.Episode;
import com.earnest.video.entity.Video;
import com.earnest.video.exception.ValueParseException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 腾讯官方的接口。
 */
@AllArgsConstructor
//TODO 腾讯的视频是分段的,待优化
public class TencentVideoAddressParser extends HttpProxyPoolSettingSupport implements VideoAddressParser {

    private static final String VIDEO_INFO_API = "http://vv.video.qq.com/getinfo?otype=json&appver=3.2.19.333&platform=%s&defnpayver=1&defn=shd&vid=%s";
    private static final String KEY_API = "http://vv.video.qq.com/getkey?otype=json&platform=11&format=%s&vid=%s&filename=%s&appver=3.2.19.333";

    private static final List<Integer> PLATFORMS = List.of(4100201, 11);

    private final HttpClient httpClient;

    private final ResponseHandler<String> responseHandler;

    private final static Pattern COVER_INFO_PATTERN = Pattern.compile("var COVER_INFO =.?(\\{.+\\})\n?var COLUMN_INFO");

    private final static Pattern VIDEO_INFO_PATTERN = Pattern.compile("var VIDEO_INFO =.*(\\{.+\\})");

    private final static Pattern OUT_PUT_JSON_PATTERN = Pattern.compile("QZOutputJson=.?(\\{.*\\})");

    @Override
    public Episode parse(String rawValue, Map<String, Object> properties) throws IOException, ValueParseException {
        Episode episode = createEpisode(rawValue, properties);
        Object vid = ((List) episode.getProperties().get("vid")).get(0);
        JSONObject jsonObject = null;
        for (Integer platform : PLATFORMS) {
            HttpUriRequest httpUriRequest = createHttpUriRequest(String.format(VIDEO_INFO_API, platform, vid));

            String responseString = httpClient.execute(httpUriRequest, responseHandler);

            Matcher matcher = OUT_PUT_JSON_PATTERN.matcher(responseString);

            if (matcher.find()) {
                jsonObject = JSONObject.parseObject(matcher.group(1));
                if (!"cannot play outside".equalsIgnoreCase(jsonObject.getString("msg"))) {
                    break;
                }
            }
        }

        if (jsonObject == null) {
            throw new ValueParseException("QZOutputJson is not found");
        }
        JSONObject vi = (JSONObject) jsonObject.getJSONObject("vl").getJSONArray("vi").get(0);
        String fnPre = vi.getString("lnk");
        String title = vi.getString("ti");
        String host = ((JSONObject) vi.getJSONObject("ul").getJSONArray("ui").get(0)).getString("url");
        int segCnt = vi.getJSONObject("cl").getIntValue("fc");
        int fcCnt = segCnt;
        String fileName = vi.getString("fn");

        String magicStr = "";
        String videoType = "";

        if (segCnt == 0) {
            segCnt = 1;
        } else {
            String[] strings = fileName.split("\\.");
            fnPre = strings[0];
            magicStr = strings[1];
            videoType = strings[2];
        }


        String[] urls = new String[segCnt];

        for (int i = 0; i < segCnt; i++) {
            String partFormatId;
            if (fcCnt == 0) {
                String[] strings = vi.getJSONObject("cl").getJSONObject("cl").getString("keyid").split("\\.");
                partFormatId = strings[strings.length - 1];
            } else {
                partFormatId = ((JSONObject) vi.getJSONObject("cl").getJSONArray("ci").get(i)).getString("keyid").split("\\.")[1];
                fileName = StringUtils.join(new String[]{fnPre, magicStr, String.valueOf(i + 1), videoType}, ".");
            }

            String keyApi = String.format(KEY_API, partFormatId, vid, fileName);
            Matcher partInfoMatcher = OUT_PUT_JSON_PATTERN.matcher(httpClient.execute(createHttpUriRequest(keyApi), responseHandler));
            if (partInfoMatcher.find()) {
                JSONObject keyJson = JSONObject.parseObject(partInfoMatcher.group(1));
                String vkey = keyJson.getString("key");
                String url;
                if (StringUtils.isBlank(vkey)) {
                    vkey = vi.getString("fvkey");
                    url = String.format("%s%s?vkey=%s", host, fnPre + ".mp4", vkey);
                } else {
                    url = String.format("%s%s?vkey=%s", host, fileName, vkey);
                }
                urls[i] = url;
            }

        }

        Video.PlayAddress playAddress = new Video.PlayAddress();
        playAddress.setType("mp4");
        playAddress.setUrl(StringUtils.join(urls, ","));


        episode.setParseValue(List.of(playAddress));

        return episode;
    }

    private Episode createEpisode(String rawValue, Map<String, Object> properties) throws IOException, ValueParseException {
        if (properties != null && properties.containsKey("vid")) {
            Episode episode = new Episode();
            episode.setProperties(properties);
            return episode;
        } else {
            return createEpisodeByParseHtml(rawValue);
        }
    }

    private Episode createEpisodeByParseHtml(String rawValue) throws IOException, ValueParseException {
        Episode episode = new Episode();
        String responseResult = httpClient.execute(createHttpUriRequest(rawValue), responseHandler);
        Matcher coverInfoMatcher = COVER_INFO_PATTERN.matcher(responseResult);
        if (coverInfoMatcher.find()) {
            JSONObject jsonObject = JSONObject.parseObject(coverInfoMatcher.group(1));
            //vid
            List vid = jsonObject.getObject("video_ids", List.class);
            Map<String, List> properties = Map.of("vid", vid);
            episode.setProperties(properties);
            episode.setCategory(Video.Category.getCategory(jsonObject.getString("type_name")));
            episode.setDescription(jsonObject.getString("description"));
            episode.setImage(jsonObject.getString("horizontal_pic_url"));

        } else
            throw new ValueParseException("COVER_INFO is not found");

        Matcher videoInfoMatcher = VIDEO_INFO_PATTERN.matcher(responseResult);
        if (videoInfoMatcher.find()) {
            JSONObject jsonObject = JSONObject.parseObject(videoInfoMatcher.group(1));
            episode.setDuration(jsonObject.getIntValue("duration"));
            episode.setTitle(jsonObject.getString("series_name") + jsonObject.getString("series_part_title"));
            episode.setShortDescription(jsonObject.getString("vname_title"));
        }


        return episode;
    }

    private static HttpUriRequest createHttpUriRequest(String rawValue) {
        return RequestBuilder.get(rawValue).setHeader(Browser.USER_AGENT, Browser.GOOGLE.userAgent()).build();
    }

    @Override
    public boolean support(String rawValue) {
        Assert.hasText(rawValue, "rawValue is required");
        return rawValue.contains("v.qq.com");
    }

    @Override
    public int priority() {
        return 5;
    }


}
