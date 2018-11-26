package com.earnest.video.core.parser;

import java.io.IOException;
import java.util.List;

@FunctionalInterface
public interface VideoAddressParser {
    List<String> parse(String playValue) throws IOException;
}
