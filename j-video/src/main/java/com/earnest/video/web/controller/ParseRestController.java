package com.earnest.video.web.controller;

import com.earnest.video.core.parser.VideoAddressParser;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.ValueExp;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/api/video")
@AllArgsConstructor
public class ParseRestController {
    private final VideoAddressParser videoAddressParser;

    @PostMapping(value="/value")
    public List<String> parseValue(String playValue) throws IOException {
        return videoAddressParser.parse(playValue);
    }

}
