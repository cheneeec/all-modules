package com.earnest.video.parser;

import com.earnest.video.Manager;
import com.earnest.video.exception.ValueParseException;
import com.earnest.video.entity.Episode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;

/**
 * 对所有的解析器进行管理。
 */
@Slf4j
public class VideoAddressParserManager implements VideoAddressParser, Manager<VideoAddressParser> {


    private final SortedSet<VideoAddressParser> videoAddressParsers = new TreeSet<>(Comparator.comparingInt(VideoAddressParser::priority));

    @Override
    public Episode parse(String rawValue, Map<String, Object> properties) throws IOException, ValueParseException {

        Set<VideoAddressParser> unavailableVideoAddressParsers = new HashSet<>(videoAddressParsers.size());

        //返回结果
        Episode result = null;
        while (result == null) {

            VideoAddressParser videoAddressParser = videoAddressParsers.stream()
                    .filter(v -> v.support(rawValue))
                    .filter(p -> !unavailableVideoAddressParsers.contains(p)) //排除不可用
                    .findFirst()
                    .orElseThrow(() -> new ValueParseException("Cannot find a parser that support " + rawValue));

            try {
                result = videoAddressParser.parse(rawValue, properties);
            } catch (ValueParseException e) {
                log.warn("Parser:{} is not available for value:{} ", videoAddressParser.getClass(), rawValue);
                unavailableVideoAddressParsers.add(videoAddressParser);
            }


        }

        return result;

    }

    @Override
    public boolean support(String rawValue) {
        return false;
    }

    @Override
    public void addWork(VideoAddressParser videoAddressParser) {
        if (videoAddressParser != null) {
            videoAddressParsers.add(videoAddressParser);
        }
    }


}
