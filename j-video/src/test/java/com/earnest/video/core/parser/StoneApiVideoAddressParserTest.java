package com.earnest.video.core.parser;

import com.earnest.crawler.Browser;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class StoneApiVideoAddressParserTest {

    StoneApiVideoAddressParser stoneApiVideoAddressParser = new StoneApiVideoAddressParser();

    @Test
    public void parse() throws IOException {

        System.out.println(stoneApiVideoAddressParser.parse("https://www.iqiyi.com/v_19rr5jax4g.html"));


    }

    /**
     * 提交参数：
     * <blockquote>
     * <pre>
     *         {
     *             time:1539100800,
     *             url:
     *             type: iqiyi
     *             pc:0
     *             fuck:
     *
     *           }
     *           time: 1539100800
     *           key: iiJHyalLjFJBzbyNdcUitR9eO-I=
     *           url: https://www.iqiyi.com/v_19rr5jax4g.html
     *           type: iqiyi
     *           pc: 0
     *           fuck: agrIa9k6C6IFMS4NtLw79GwZ5NTsV39yHcmX2AmLtag
     *     </pre>
     *
     * </blockquote>
     *
     * @throws Exception
     */
    @Test
    public void httpClientParse() throws Exception {
        // 1.创建连接
        CloseableHttpClient httpClient = HttpClients.createDefault();

        BasicResponseHandler responseHandler = new BasicResponseHandler();
        HttpUriRequest httpGet = RequestBuilder.get("http://jiexi.071811.cc/jx2.php?url=https://www.iqiyi.com/v_19rr5jax4g.html")
                .addHeader(Browser.USER_AGENT, Browser.GOOGLE.userAgent())
                .build();
        //2. 创建cookie存储
        CookieStore cookieStore = new BasicCookieStore();

        Map<String, String> cookies = new LinkedHashMap<>();
        cookies.put("CNZZDATA1259275400", "1958109893-1539138416-%7C1539138416");
        cookies.put("Hm_lpvt_4460507abd2c41a601e451bf3aa9bb81", "1539140747");
        cookies.put("Hm_lvt_4460507abd2c41a601e451bf3aa9bb81", "1539140658");
        cookies.put("PHPSESSID", "vs22a6rcruoc9qr3dkia1o2bb2");
        cookies.put("UM_distinctid", "1665beea22e404-0e6fa8b11a6e86-3c7f0257-100200-1665beea22fa8d");
        cookies.entrySet()
                .stream()
                .map(e -> new BasicClientCookie(e.getKey(), e.getValue()))
                .forEach(cookieStore::addCookie);

        //3. 创建上下文
        HttpClientContext context = new HttpClientContext();
        context.setCookieStore(cookieStore);


        String execute = httpClient.execute(httpGet, responseHandler, context);

        //4. 查看内容
        System.out.println("=========cookies=========");
        context.getCookieStore().getCookies().stream()
                .map(c -> c.getName() + ":" + c.getValue())
                .forEach(System.out::println);
        System.out.println("=========headers=========");
        Arrays.stream(context.getResponse().getAllHeaders())
                .map(h -> h.getName() + ":" + h.getValue())
                .forEach(System.out::println);
        System.out.println("=========response=========");
        System.out.println(execute);


        System.out.println("=========the second request============");  //
        HttpUriRequest httpGet3 = RequestBuilder.copy(httpGet).setUri("http://jiexi.071811.cc/stapi.php?url=https://www.iqiyi.com/v_19rr5jax4g.html").build();
        System.out.println("=========the second response============");
        System.out.println(httpClient.execute(httpGet3, responseHandler, context));


    }

    @Test
    public void seleniumTest() {

    }
}