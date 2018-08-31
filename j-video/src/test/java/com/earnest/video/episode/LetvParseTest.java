package com.earnest.video.episode;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.core.Browser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LetvParseTest {
    private final static String ROUTE = "http://player-pc.le.com/mms/out/video/playJson.json?platid=3&splatid=304&tss=no&id=%s&detect=1&dvtype=1300&accessyx=1&domain=www.le.com&tkey=%s&devid=b452a9ce08e7d08370dcdf12aba639c1&source=1001&lang=cn&region=cn&isHttps=0";
    private final static String VIP_LOCATION = "%s%s&token=%s&uid=67945963&format=1&jsonp=vjs_149067353337651&expect=3&p1=0&p2=06&termid=2&ostype=macos&hwtype=un&uuid=1891087902108532_1&vid=%s&";
    private static final String LETV_VIDEOS = "http://d.api.m.le.com/apipccard/dynamic?cid=2&vid=%s&platform=pc&isvip=1&type=episode,Cotherlist";
    private final static String VID_REGEX = "([0-9]+)\\.html";
    private final static String[] DIS_LIST = {"1300", "1000", "350"};

    private CloseableHttpClient httpClient = HttpClients.createDefault();

    public String parse(String url) throws IOException {
        String vid = this.matchVid(url);
        String routeUrl = String.format(ROUTE, vid, getTkey());

        /*Document document = Jsoup.connect(routeUrl)
                .userAgent(Browser.IPHONE_X.userAgent())
                .get();*/
        HttpUriRequest httpUriRequest = RequestBuilder.get(routeUrl)
                .setHeader(Browser.USER_AGENT, Browser.IPHONE_X.userAgent())
                .build();

        String execute = httpClient.execute(httpUriRequest, new BasicResponseHandler());


        JSONObject object = JSONObject.parseObject(execute);
        JSONObject playurl = object.getJSONObject("msgs").getJSONObject("playurl");
        String image = playurl.getString("pic").replace("120_90", "360_180");
        String domain = playurl.getJSONArray("domain").getString(0);
        String dispatch = getDispatch(playurl.getJSONObject("dispatch"));
        JSONObject yuanxian = object.getJSONObject("msgs").getJSONObject("yuanxian");
        String locationUrl;
        if (yuanxian != null) {
            String token = yuanxian.getString("token");
            locationUrl = String.format(VIP_LOCATION, domain, dispatch, token, vid);
        } else {
            locationUrl = String.format(VIP_LOCATION, domain, dispatch, "", vid);
        }
        Document result = Jsoup.connect(locationUrl)
                .userAgent(Browser.IPHONE_X.userAgent())
                .get();
        String text = StringEscapeUtils.unescapeJava(result.text());
        text = text.replace("vjs_149067353337651(", "");
        text = text.replace(");", "");
        JSONObject videoJson = JSONObject.parseObject(text);

        return videoJson.getJSONArray("nodelist").getJSONObject(0).getString("location");
    }

  /*  public List<Episode> parseEpisodes(String videoUrl) {
        List<Episode> episodes = new ArrayList<>();
        Document document = JsoupUtils.getDocWithPhone(videoUrl);
        Matcher matcher = Pattern.compile("([0-9]{5,})\\.html").matcher(document.html());
        if (matcher.find()) {
            String vid = matcher.group(1);
            String videosAPI = String.format(LETV_VIDEOS, vid);
            String data = JsoupUtils.getDocWithPhone(videosAPI).body().text();
            JSONObject jsonObject = JSONObject.parseObject(data);
            JSONArray array = jsonObject.getJSONObject("data").getJSONObject("episode").getJSONArray("videolist");
            if (array.size() > 1) {
                for (int i = array.size() - 1; i >= 0; i--) {
                    JSONObject object = array.getJSONObject(i);
                    Episode episode = new Episode();
                    Integer index = object.getInteger("episode");
                    if (index < 10) {
                        episode.setIndex("0" + index);
                    } else {
                        episode.setIndex("" + index);
                    }
                    episode.setValue(object.getString("url"));
                    episodes.add(episode);
                }
            }
        }
        return episodes;
    }*/

    /**
     * 从 URL 中匹配 VID
     */
    private String matchVid(String videoUrl) throws IOException {
        Matcher matcher = Pattern.compile(VID_REGEX).matcher(videoUrl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            Document realDocument = Jsoup.connect(videoUrl).userAgent(Browser.GOOGLE.userAgent()).get();
            matcher = Pattern.compile("vid:\"(.*?)\"").matcher(realDocument.html());
            if (matcher.find())
                return matcher.group(1);
        }
        return null;
    }

    /**
     * 获取最清晰的视频线路
     */
    private String getDispatch(JSONObject dispatch) {
        for (String dis : DIS_LIST) {
            if (dispatch.containsKey(dis)) {
                return dispatch.getJSONArray(dis).getString(0);
            }
        }
        return null;
    }

    /**
     * 乐视tkey算法
     */
    private static String getTkey() {
        int a = (int) (new Date().getTime() / 1000);
        for (int i = 0; i < 8; i++) {
            int b = a >> 1;
            int c = (0x1 & a) << 31;
            a = b + c;
        }
        int result = 0xB074319 ^ a;
        return "" + result;
    }

    @Test
    public void parse() throws Exception {
        LetvParseTest letvParseTest = new LetvParseTest();
        String parse = letvParseTest.parse("http://www.le.com/ptv/vplay/2154216.html");
        System.out.println(parse);
    }
}
