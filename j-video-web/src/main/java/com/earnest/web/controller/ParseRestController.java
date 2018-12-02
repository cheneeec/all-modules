package com.earnest.web.controller;

import com.earnest.video.exception.ValueParseException;
import com.earnest.video.parser.VideoAddressParser;
import com.earnest.video.entity.Video;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/video")
@AllArgsConstructor
public class ParseRestController {

    private final VideoAddressParser videoAddressParser;

    @PostMapping(value = "/value")
    public Video parseValue(String playValue,@RequestParam(required = false) Map<String, Object> properties) throws IOException, ValueParseException {
        return videoAddressParser.parse(playValue, properties);
    }

}
