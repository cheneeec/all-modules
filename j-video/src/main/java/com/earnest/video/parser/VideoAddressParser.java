package com.earnest.video.parser;


import java.io.IOException;

@FunctionalInterface
public interface VideoAddressParser {

    String parse(String playValue) throws IOException;
}
