package com.earnest.video.spider;

import com.earnest.crawler.core.crawler.Spider;
import com.earnest.crawler.core.crawler.SpiderBuilder;
import com.earnest.crawler.core.crawler.Spiders;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.HttpRequest;
import com.earnest.crawler.core.response.HttpResponse;
import com.earnest.video.entity.IQiYi;
import com.earnest.video.service.VideoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class IQiYiSpider implements Spider, CommandLineRunner {
    //动漫的爬虫
    private final Spider animationSpider;
    //电影爬虫
    private final Spider movieSpider;

    private final VideoService videoService;

    private final AtomicLong atomicLong = new AtomicLong();

    public IQiYiSpider(VideoService videoService) {
        animationSpider = createSpider(createAnimationPipeline(), "http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html", "/www/4/38-------------4-\\d+-1-iqiyi--.html", 5)
                .stopWhen(createAnimationStopWhenPredicate())
                .build();
        animationSpider.setName("iQiYi-animation");
        movieSpider = createSpider(createMoviePipeline(), "http://www.iqiyi.com/dianying_new/i_list_paihangbang.html", null, 1).build();
        movieSpider.setName("iQiYi-movie");
        this.videoService = videoService;
    }

    private Predicate<HttpResponse> createAnimationStopWhenPredicate() {
        return httpResponse -> {
            Element body = Jsoup.parse(httpResponse.getContent()).body();
            return body.select("body > div.mod-page > span.noPage").attr("class").equals("noPage");
        };
    }

    private Pipeline<List<IQiYi>> createMoviePipeline() {
        return httpResponse -> {
            Element element = Jsoup.parse(httpResponse.getContent()).body();
            Elements elements = element.select("#widget-tab-0 > div.piclist-scroll.piclist-scroll-h290 > div > div:nth-child(1) > ul > li:not(li.J_videoLi.first_bigImg)");
            final int[] i = {1};
            return elements.stream().map(e -> {
                IQiYi iQiYi = newIQiYi(httpResponse.getHttpRequest());
                Elements a = e.select("div.site-piclist_pic > a");
                iQiYi.setPlayValue(a.attr("href"));
                iQiYi.setTitle(a.attr("title"));
//                iQiYi.setPlayInfo(a.select("span.icon-vInfo").text());
                iQiYi.setImage(a.select("img").attr("src"));
                iQiYi.setCategory("电影");
                return iQiYi;
            }).collect(Collectors.toList());
        };
    }


    private IQiYi newIQiYi(HttpRequest httpRequest) {
        IQiYi iQiYi = new IQiYi();
        iQiYi.setFromUrl(httpRequest.getUrl());
        iQiYi.setCollectTime(Calendar.getInstance().getTime());
        iQiYi.setId(atomicLong.incrementAndGet());
        return iQiYi;
    }

    private Pipeline<List<IQiYi>> createAnimationPipeline() {
        return httpResponse -> {
            Element element = Jsoup.parse(httpResponse.getContent()).body();
            Elements elements = element.select("body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li");
            return elements.stream().map(e -> {
                IQiYi iQiYi = newIQiYi(httpResponse.getHttpRequest());
                Elements a = e.select("div.site-piclist_pic > a");
                iQiYi.setPlayValue("http:" + a.attr("href"));
                iQiYi.setImage("http:" + a.select("img").attr("src"));
                iQiYi.setTitle(a.select("img").attr("title"));
                iQiYi.setPlayInfo(a.select("span.icon-vInfo").text());
                iQiYi.setCategory("动漫");
                return iQiYi;
            }).collect(Collectors.toList());
        };
    }

    private SpiderBuilder createSpider(Pipeline<List<IQiYi>> pipeline, String from, String match, int threadNumber) {
        return Spiders.createCustom()
                .from(from)
                .match(match)
                .pipeline(pipeline)
                .addConsumer(o -> videoService.save(((List<IQiYi>) o)))
                .thread(threadNumber);

    }


    @Override
    public void start() {
        animationSpider.start();
        movieSpider.start();
    }

    @Override
    public void shutdown() {
        animationSpider.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        ArrayList<Runnable> runnables = new ArrayList<>(movieSpider.shutdownNow());
        runnables.addAll(animationSpider.shutdownNow());
        return runnables;
    }

    @Override
    public boolean isRunning() {
        return animationSpider.isRunning()||movieSpider.isRunning();
    }

    @Override
    public String getName() {
        return "IQiYi";
    }

    @Override
    public void setName(String name) {
        //ignored
    }

    @Override
    public void close() throws IOException {
        animationSpider.close();
        movieSpider.close();
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }
}
