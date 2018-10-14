package com.earnest.video.core.parser;


import java.io.IOException;

@FunctionalInterface
public interface VideoAddressParser {
    String parse(String playValue) throws IOException;
}
