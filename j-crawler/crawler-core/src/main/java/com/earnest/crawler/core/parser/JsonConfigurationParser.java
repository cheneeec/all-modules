package com.earnest.crawler.core.parser;

import com.alibaba.fastjson.JSONObject;
import com.earnest.crawler.core.handler.HttpResponseHandler;
import com.earnest.crawler.core.handler.RegexHttpResponseHandler;
import com.earnest.crawler.core.pipe.Pipeline;
import com.earnest.crawler.core.request.AbstractHttpRequest;
import com.earnest.crawler.core.request.HttpCustomRequest;
import com.earnest.crawler.core.request.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class JsonConfigurationParser implements Parser {

    private HttpResponseHandler httpResponseHandler;
    private Pipeline<?> pipeline;
    private Set<HttpRequest> httpRequests = new HashSet<>(6);


    public JsonConfigurationParser(String jsonConfigurationPath) {
        Resource pathResource = new ClassPathResource(jsonConfigurationPath);
        try {
            InputStream jsonConfigurationInputStream = new ClassPathResource(jsonConfigurationPath).getInputStream();
            doParse(jsonConfigurationInputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException(pathResource.getFilename() + "is not found");
        }

    }

    private void doParse(InputStream jsonConfigurationInputStream) throws IOException {

        JSONObject jsonConfiguration = JSONObject.parseObject(jsonConfigurationInputStream, JSONObject.class);
        //get from
        extractHttpRequest(jsonConfiguration);

        extractHttpResponseHandler(jsonConfiguration.getString("match"));

        extractPipeline(jsonConfiguration.getJSONObject("pipeline"));


    }

    @SuppressWarnings("unchecked")
    private void extractPipeline(JSONObject pipelineJsonObject) {
        String strip = pipelineJsonObject.getString("strip");
        Map<String, String> entityMap = pipelineJsonObject.getObject("entity", Map.class);

        pipeline = httpResponse -> {
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


    private void extractHttpResponseHandler(String match) {
        httpResponseHandler = new RegexHttpResponseHandler(match);
    }

    @SuppressWarnings("unchecked")
    private void extractHttpRequest(JSONObject jsonConfiguration) {
        //set HttpRequest
        JSONObject requestJsonObject = jsonConfiguration.getJSONObject("request");
        AbstractHttpRequest request = new HttpCustomRequest(requestJsonObject.getString("method"));
        request.setUrl(jsonConfiguration.getString("from"));
        request.setParameters(requestJsonObject.getObject("parameters", Map.class));
        request.setHeaders(requestJsonObject.getObject("headers", Map.class));
        request.setCookies(requestJsonObject.getObject("cookies", Map.class));
        request.setCharset(requestJsonObject.getString("charset"));

        httpRequests.add(request);
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
    public void close() throws IOException {

    }

}
