package com.earnest.crawler.core.parser;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.handler.RegexHttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.AbstractHttpRequest;
import com.earnest.crawler.core.request.HttpCustomRequest;
import com.earnest.crawler.core.request.HttpRequest;
import lombok.Cleanup;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

/**
 * {
 *   "from": "http://list.iqiyi.com/www/4/38-------------4-1-1-iqiyi--.html",
 *   "thread": 5,
 *   "match": "/www/4/38-------------4-\\d-1-iqiyi--.html",
 *   "consumer": [
 *     ""
 *   ],
 *   "request": {
 *     "method": "GET",
 *     "charset": "UTF-8",
 *     "cookies": {
 *       "__uuid": "48c04310-fe61-4b7e-d7ba-2178d31b2ea5"
 *     },
 *     "headers": {
 *       "_1_auth": "b7zDuUiMlnJGF8BArM0Eg0LH2vbgBv",
 *       "_1_ver": "0.3.0",
 *       "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"
 *     },
 *     "parameters": {}
 *   },
 *   "pipeline": {
 *     "strip": "body > div.page-list.page-list-type1 > div > div > div.wrapper-cols > div > ul > li",
 *     "entity": {
 *       "image": "div.site-piclist_pic > a > img->src",
 *       "title": "div.site-piclist_pic > a > img->title",
 *       "href": "div.site-piclist_pic > a->abs:href"
 *     }
 *   }
 * }
 */
public class JsonConfigurationParser implements Parser {

    private HttpResponseHandler httpResponseHandler;
    private Pipeline<?> pipeline;
    private Set<HttpRequest> httpRequests = new HashSet<>(6);
    private Set<?> persistenceConsumers;
    private int threadNumber;


    public JsonConfigurationParser(String jsonConfigurationPath) {
        Resource pathResource = new ClassPathResource(jsonConfigurationPath);
        try {
            @Cleanup InputStream jsonConfigurationInputStream = new ClassPathResource(jsonConfigurationPath).getInputStream();
            doParse(jsonConfigurationInputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException(pathResource.getFilename() + "is not found,error" + e.getMessage());
        }

    }

    private void doParse(InputStream jsonConfigurationInputStream) throws IOException {

        JSONObject jsonConfiguration = JSONObject.parseObject(jsonConfigurationInputStream, JSONObject.class);
        //take from
        httpRequests.add(extractHttpRequest(jsonConfiguration));

        httpResponseHandler = extractHttpResponseHandler(jsonConfiguration.getString("match"));

        pipeline = extractPipeline(jsonConfiguration.getJSONObject("pipeline"));

        threadNumber = extractThreadNumber(jsonConfiguration.getIntValue("thread"));

        persistenceConsumers = extractPersistenceConsumers(jsonConfiguration.get("consumer"));

    }

    private Set<?> extractPersistenceConsumers(Object consumer) {
        try {
            if (isNull(consumer)) {
                return null;
            }
            if (consumer instanceof String && StringUtils.isNotBlank(((String) consumer))) {
                return Collections.singleton(ClassUtils.getClass(((String) consumer)).newInstance());
            }
            if (consumer instanceof Iterable) {
                Set<Consumer> temp = new HashSet<>();
                for (Object o : ((Iterable) consumer)) {
                    String s = (String) o;
                    if (StringUtils.isNotBlank(s)) {
                        temp.add(((Consumer) ClassUtils.getClass(s).newInstance()));
                    }
                }
                return temp;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error in configuration file,error:" + e.getMessage());
        }

        return null;
    }

    private int extractThreadNumber(int thread) {
        return thread == 0 ? 1 : thread;
    }


    @SuppressWarnings("unchecked")
    private Pipeline<?> extractPipeline(JSONObject pipelineJsonObject) {
        String strip = pipelineJsonObject.getString("strip");
        Map<String, String> entityMap = pipelineJsonObject.getObject("entity", Map.class);

        return httpResponse -> {
            Element element = Jsoup.parse(httpResponse.getContent(), httpResponse.getHttpRequest().getCharset())
                    .body();
            if (StringUtils.isNotBlank(strip)) {
                Elements elements = element.select(strip);
                return elements.stream()
                        .map(doElementParse(entityMap)).collect(toList());
            }
            return Collections.singletonList(doElementParse(entityMap));
        };
    }

    private Function<Element, JSONObject> doElementParse(Map<String, String> entityMap) {
        return e -> {
            JSONObject entity = new JSONObject(entityMap.size());
            for (String property : entityMap.keySet()) {
                String[] cssSelector = entityMap.get(property).split("->");
                Assert.state(cssSelector.length == 2, "The configuration file is wrong,error:" + entityMap.get(property));
                if (StringUtils.equalsIgnoreCase(cssSelector[1], "text")) {
                    entity.put(property, e.select(cssSelector[0]).text());
                } else {
                    entity.put(property, e.select(cssSelector[0]).attr(cssSelector[1]));
                }
            }
            return entity;
        };
    }


    private HttpResponseHandler extractHttpResponseHandler(String match) {
        return new RegexHttpResponseHandler(match);
    }

    @SuppressWarnings("unchecked")
    private HttpRequest extractHttpRequest(JSONObject jsonConfiguration) {
        //set HttpRequest
        JSONObject requestJsonObject = jsonConfiguration.getJSONObject("request");
        AbstractHttpRequest request = new HttpCustomRequest(requestJsonObject.getString("method"));
        request.setUrl(jsonConfiguration.getString("from"));
        request.setParameters(requestJsonObject.getObject("parameters", Map.class));
        request.setHeaders(requestJsonObject.getObject("headers", Map.class));
        request.setCookies(requestJsonObject.getObject("cookies", Map.class));
        request.setCharset(requestJsonObject.getString("charset"));

        return request;
    }


    @Override
    public HttpResponseHandler getHttpResponseHandler() {
        return httpResponseHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Pipeline<T> getPipeline() {
        return (Pipeline<T>) pipeline;
    }

    @Override
    public Set<HttpRequest> getHttpRequests() {
        return httpRequests;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> getPersistenceConsumers() {
        return (Set<T>) persistenceConsumers;
    }

    @Override
    public int getThread() {
        return threadNumber;
    }

    @Override
    public void close() {
        //ignored
    }
}
