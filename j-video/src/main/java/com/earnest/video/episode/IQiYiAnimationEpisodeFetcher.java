package com.earnest.video.episode;

import com.earnest.crawler.core.request.BrowserUserAgent;
import com.earnest.video.entity.Episode;
import org.apache.http.HttpRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import sun.net.www.http.HttpClient;

import java.util.List;

//http://cache.video.iqiyi.com/jp/avlist/202861101/16/50/?albumId=202861101&pageNum=50&pageNo=16&callback=window.Q.__callbacks__.cbvsvgw5
//http://cache.video.iqiyi.com/jp/avlist/202861101/4/50/?albumId=202861101&pageNum=50&pageNo=4&callback=window.Q.__callbacks__.cby3hijk
public class IQiYiAnimationEpisodeFetcher implements EpisodeFetcher {
    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    @Override
    public List<Episode> fetch(String url, int page, int size) {
        return null;
    }

    private String getAlbumId(String url) {

        Jsoup.connect(url)
                .userAgent(BrowserUserAgent.GOOGLE.value())
                .ignoreContentType(true)
                .referrer(url);



        return null;
//                .userAgent()
//        GET /jp/avlist/202783001/2/50/?albumId=202783001&pageNum=50&pageNo=2&callback=window.Q.__callbacks__.cbv9b7l9 HTTP/1.1
//        Host: cache.video.iqiyi.com
//        Connection: keep-alive
//        User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Safari/537.36
//        Accept: */*
//Referer: http://www.iqiyi.com/a_19rrhb5shh.html
//Accept-Encoding: gzip, deflate
//Accept-Language: zh-CN,zh;q=0.9
//Cookie: QC005=071c7e191978481b462bf9a1f7a5b773; Hm_lvt_53b7374a63c37483e5dd97d78d9bb36e=1531981912; QC173=0; QC007=DIRECT; QC006=zy0zqltsun17zyzdshphgka; T00404=e8d514b270d674ead85a053594c295a8; QC008=1531981912.1531981912.1531981916.2; nu=0; PCAU=0; QC001=1; P00004=1987248083.1531981953.9d97a16d3e; QP001=1; QC159=%7B%22color%22%3A%22FFFFFF%22%2C%22channelConfig%22%3A0%7D; QC118=%7B%22color%22%3A%22FFFFFF%22%2C%22channelConfig%22%3A0%7D; QILINPUSH=1; Hm_lpvt_53b7374a63c37483e5dd97d78d9bb36e=1531990329; QC010=90540121; __dfp=a0785c592695a54743b70e631c2969208afed169bda193701c4bbfca8cb035abbc@1533277917692@1531981917692


    }



}
