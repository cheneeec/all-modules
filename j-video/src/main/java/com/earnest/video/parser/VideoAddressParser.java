package com.earnest.video.parser;

import com.earnest.video.entity.BaseVideoEntity;

import java.io.IOException;

@FunctionalInterface
public interface VideoAddressParser {

    <T extends BaseVideoEntity> BaseVideoEntity parse(T t) throws IOException;
}
