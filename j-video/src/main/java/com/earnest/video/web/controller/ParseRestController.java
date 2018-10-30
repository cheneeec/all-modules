package com.earnest.video.web.controller;

import com.earnest.video.core.parser.VideoAddressParser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class ParseRestController {
    private final VideoAddressParser videoAddressParser;

    @GetMapping("/value")
    public String parseValue( String playValue) throws IOException {

        return videoAddressParser.parse(playValue);
    }

}
