package com.earnest.video.controller;

import com.earnest.video.parser.VideoAddressParser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class ParseController {
    private final VideoAddressParser videoAddressParser;

    @GetMapping("/value")
    public String parseValue(@NotBlank String playValue) throws IOException {

        return videoAddressParser.parse(playValue);
    }

}
