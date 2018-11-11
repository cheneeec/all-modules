package com.earnest.video.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.earnest.video.core.episode.EpisodeFetcher;
import com.earnest.video.entity.Episode;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/episode")
@AllArgsConstructor
public class EpisodeRestController {


    private final EpisodeFetcher episodeFetcher;

    @GetMapping(value = "/query")
    public List<Episode> findEpisodes(String url, @PageableDefault(size = 50) Pageable page) throws IOException {

//        return episodeFetcher.fetch(url, page);
        return JSONObject.parseArray("[\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699648700\",\n" +
                "\t\t\"image\":\"http://pic0.iqiyipic.com/image/20170614/49/ab/v_112535138_m_601.jpg\",\n" +
                "\t\t\"number\":1,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr7552og.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第1集\",\n" +
                "\t\t\"vId\":\"1a48521c0e2afd744176d5d685cb1b27\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699665900\",\n" +
                "\t\t\"image\":\"http://pic3.iqiyipic.com/image/20170614/25/27/v_112535135_m_601.jpg\",\n" +
                "\t\t\"number\":2,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr758x34.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第2集\",\n" +
                "\t\t\"vId\":\"aace5f546baa5c60b0040e2a7fe3aba8\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699676800\",\n" +
                "\t\t\"image\":\"http://pic1.iqiyipic.com/image/20170614/82/62/v_112535139_m_601.jpg\",\n" +
                "\t\t\"number\":3,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr758uzw.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第3集\",\n" +
                "\t\t\"vId\":\"ed57d27260c3e0c53457680b2149b9e0\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699685100\",\n" +
                "\t\t\"image\":\"http://pic1.iqiyipic.com/image/20170614/11/db/v_112535137_m_601.jpg\",\n" +
                "\t\t\"number\":4,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr758olc.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第4集\",\n" +
                "\t\t\"vId\":\"44aac057638a1bbc8eaa500f267d79a1\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699689400\",\n" +
                "\t\t\"image\":\"http://pic2.iqiyipic.com/image/20170614/29/cc/v_112535136_m_601.jpg\",\n" +
                "\t\t\"number\":5,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr758ez8.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第5集\",\n" +
                "\t\t\"vId\":\"4892e13acef6557b6b574725946d65be\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699691900\",\n" +
                "\t\t\"image\":\"http://pic2.iqiyipic.com/image/20170614/cd/64/v_112535134_m_601.jpg\",\n" +
                "\t\t\"number\":6,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr758g0g.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第6集\",\n" +
                "\t\t\"vId\":\"4506c4376eff025f84da283e51349775\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699693600\",\n" +
                "\t\t\"image\":\"http://pic1.iqiyipic.com/image/20170614/99/28/v_112535131_m_601.jpg\",\n" +
                "\t\t\"number\":7,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr758hu4.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第7集\",\n" +
                "\t\t\"vId\":\"a02ced7dbc3a64e309aa179233de8af8\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699695600\",\n" +
                "\t\t\"image\":\"http://pic0.iqiyipic.com/image/20170614/6d/e3/v_112535132_m_601.jpg\",\n" +
                "\t\t\"number\":8,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759llo.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第8集\",\n" +
                "\t\t\"vId\":\"94a85d19fc557ab80aa4f0a807694dde\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699697700\",\n" +
                "\t\t\"image\":\"http://pic7.iqiyipic.com/image/20170614/cf/76/v_112535115_m_601.jpg\",\n" +
                "\t\t\"number\":9,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759mx4.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1396,\n" +
                "\t\t\"title\":\"爆战弹珠人 第9集\",\n" +
                "\t\t\"vId\":\"a1099ec1a2c21f69e8ae33e2b3195f14\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699699700\",\n" +
                "\t\t\"image\":\"http://pic1.iqiyipic.com/image/20170614/8a/c8/v_112535133_m_601.jpg\",\n" +
                "\t\t\"number\":10,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759orc.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第10集\",\n" +
                "\t\t\"vId\":\"775dc51856f8ad0663387a9ed6d20e4e\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699700800\",\n" +
                "\t\t\"image\":\"http://pic0.iqiyipic.com/image/20170614/47/70/v_112535127_m_601.jpg\",\n" +
                "\t\t\"number\":11,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759nos.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第11集\",\n" +
                "\t\t\"vId\":\"1954b20e8c1d041ff4b36c6b0c32cc8d\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699701800\",\n" +
                "\t\t\"image\":\"http://pic9.iqiyipic.com/image/20170614/de/a1/v_112535126_m_601.jpg\",\n" +
                "\t\t\"number\":12,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759q2s.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第12集\",\n" +
                "\t\t\"vId\":\"041189baf9e3d37d97305b9fc1d80f92\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699703300\",\n" +
                "\t\t\"image\":\"http://pic4.iqiyipic.com/image/20170614/11/9b/v_112535130_m_601.jpg\",\n" +
                "\t\t\"number\":13,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759ffs.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1398,\n" +
                "\t\t\"title\":\"爆战弹珠人 第13集\",\n" +
                "\t\t\"vId\":\"285c310e4c6909185cdb13683737fa99\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699704500\",\n" +
                "\t\t\"image\":\"http://pic4.iqiyipic.com/image/20170614/43/30/v_112535129_m_601.jpg\",\n" +
                "\t\t\"number\":14,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759eqg.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第14集\",\n" +
                "\t\t\"vId\":\"0b0d40209cce8c41c1704dcf1b8ebde3\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699705000\",\n" +
                "\t\t\"image\":\"http://pic5.iqiyipic.com/image/20170614/18/10/v_112535128_m_601.jpg\",\n" +
                "\t\t\"number\":15,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759ebo.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第15集\",\n" +
                "\t\t\"vId\":\"c0efc0523908a4d5a9cda00dc801036a\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699705900\",\n" +
                "\t\t\"image\":\"http://pic8.iqiyipic.com/image/20170614/bb/3c/v_112535114_m_601.jpg\",\n" +
                "\t\t\"number\":16,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759glc.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第16集\",\n" +
                "\t\t\"vId\":\"2a22a18ff3c66b36ead8f2b9748a0230\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699707700\",\n" +
                "\t\t\"image\":\"http://pic3.iqiyipic.com/image/20170614/50/68/v_112535124_m_601.jpg\",\n" +
                "\t\t\"number\":17,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759ie0.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1397,\n" +
                "\t\t\"title\":\"爆战弹珠人 第17集\",\n" +
                "\t\t\"vId\":\"aace2ca3015e6b8c14f5356ef8e57cb3\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699709600\",\n" +
                "\t\t\"image\":\"http://pic1.iqiyipic.com/image/20170614/a7/84/v_112535122_m_601.jpg\",\n" +
                "\t\t\"number\":18,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759k98.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1398,\n" +
                "\t\t\"title\":\"爆战弹珠人 第18集\",\n" +
                "\t\t\"vId\":\"4bdfa42b8b799fcaa04b58a7420e7d04\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699710900\",\n" +
                "\t\t\"image\":\"http://pic8.iqiyipic.com/image/20170614/a2/6f/v_112535125_m_601.jpg\",\n" +
                "\t\t\"number\":19,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759ja0.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1398,\n" +
                "\t\t\"title\":\"爆战弹珠人 第19集\",\n" +
                "\t\t\"vId\":\"43f5096272f081100a9aef3488adac91\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699713300\",\n" +
                "\t\t\"image\":\"http://pic4.iqiyipic.com/image/20170614/8b/6e/v_112535123_m_601.jpg\",\n" +
                "\t\t\"number\":20,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr7597qw.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1398,\n" +
                "\t\t\"title\":\"爆战弹珠人 第20集\",\n" +
                "\t\t\"vId\":\"622e103d63364127ecc43c1f2cb95539\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699714800\",\n" +
                "\t\t\"image\":\"http://pic6.iqiyipic.com/image/20170614/b5/c4/v_112535116_m_601.jpg\",\n" +
                "\t\t\"number\":21,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr7599y4.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1398,\n" +
                "\t\t\"title\":\"爆战弹珠人 第21集\",\n" +
                "\t\t\"vId\":\"5be4c8ffaf07f8fb80c0531473fc86b1\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699716300\",\n" +
                "\t\t\"image\":\"http://pic0.iqiyipic.com/image/20170614/3b/9c/v_112535121_m_601.jpg\",\n" +
                "\t\t\"number\":22,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759bxc.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第22集\",\n" +
                "\t\t\"vId\":\"f8d521e77823133c44d17f362da87324\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699717200\",\n" +
                "\t\t\"image\":\"http://pic1.iqiyipic.com/image/20170614/db/24/v_112535120_m_601.jpg\",\n" +
                "\t\t\"number\":23,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759b24.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第23集\",\n" +
                "\t\t\"vId\":\"f9e05e6c9c5055a10cf038fa50c85e6d\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699717700\",\n" +
                "\t\t\"image\":\"http://pic5.iqiyipic.com/image/20170614/33/64/v_112535117_m_601.jpg\",\n" +
                "\t\t\"number\":24,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759dt4.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1400,\n" +
                "\t\t\"title\":\"爆战弹珠人 第24集\",\n" +
                "\t\t\"vId\":\"a6e47acfc9bd5facf3f2a0ceb03b808f\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699719600\",\n" +
                "\t\t\"image\":\"http://pic7.iqiyipic.com/image/20170614/67/f8/v_112535119_m_601.jpg\",\n" +
                "\t\t\"number\":25,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr759ckc.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1399,\n" +
                "\t\t\"title\":\"爆战弹珠人 第25集\",\n" +
                "\t\t\"vId\":\"220fc8371a87f313bea280cce59c12bd\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"description\":\"弹珠人是对由玩具元老厂商TAKARA发售的腹部可以安装并发射弹珠，用以对战和竞技的玩具系列，始于1990年代，在日本大热卖后在国内产生了不少模仿产品，是很多人的童年回忆。《B-传说！战斗弹珠人》和《爆战弹珠人》分别对应两代产品的动画节目，作为产业动画投入非常高，因此即便抛开玩具因素，单纯当做热血少年动画也拥有很高的水平，曾在香港和沿海一带城市播出。\",\n" +
                "\t\t\"id\":\"699720400\",\n" +
                "\t\t\"image\":\"http://pic6.iqiyipic.com/image/20170614/28/47/v_112535118_m_601.jpg\",\n" +
                "\t\t\"number\":26,\n" +
                "\t\t\"playValue\":\"http://www.iqiyi.com/v_19rr7592gs.html\",\n" +
                "\t\t\"shortDescription\":\"\",\n" +
                "\t\t\"timeLength\":1398,\n" +
                "\t\t\"title\":\"爆战弹珠人 第26集\",\n" +
                "\t\t\"vId\":\"64e38df32f872ab484b93c4f64f49301\"\n" +
                "\t}\n" +
                "]", Episode.class);
    }


}
