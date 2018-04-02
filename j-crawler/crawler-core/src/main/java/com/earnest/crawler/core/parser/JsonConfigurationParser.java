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
    public int getThreadNumber() {
        return threadNumber;
    }

    @Override
    public void close() throws IOException {

    }
}
