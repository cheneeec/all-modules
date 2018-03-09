package com.earnest.crawler.core.downloader;


import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class DownloaderTest {

    @Test
    public void download() {
        Downloader downloader = new HttpClientDownloader(HttpClients.createDefault());
        HttpGetRequest httpRequest=new HttpGetRequest("https://www.baidu.com/");
        httpRequest.setHeaders(new HashMap<String, String>(1){{
            put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            put("Cookie","BDUSS=nlHMk44YWlqSG91NEgzbGNPcUgzMnAxaThUNlVtdlZTNU9XUjhFdWs4R3dsYWhhQVFBQUFBJCQAAAAAAAAAAAEAAADh7i6dyM~V5jHIrQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAALAIgVqwCIFaf; BIDUPSID=59507D6967468CC16EE869A08962482C; PSTM=1520215909; BD_UPN=12314753; BAIDUID=251A68357B29BCFCEB5ABE74F495753E:FG=1; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598; BD_CK_SAM=1; PSINO=3; BD_HOME=1; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; H_PS_PSSID=1441_21102_17001_20881_20928; sug=3; sugstore=0; ORIGIN=2; bdime=0");
        }});
        HttpResponse download = downloader.download(httpRequest);
        System.out.println(download.getContent());
        assertNotNull(download.getContent());

    }

    @Test
    public void shutdown() {
    }
}