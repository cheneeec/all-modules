package com.earnest.video.parser;

import org.junit.Test;

import java.io.IOException;


public class StoneApiVideoAddressParserTest {
    StoneApiVideoAddressParser stoneApiVideoAddressParser = new StoneApiVideoAddressParser();

    @Test
    public void parse() throws IOException {

        System.out.println(stoneApiVideoAddressParser.parse("https://www.iqiyi.com/v_19rr5jax4g.html"));


    }
}