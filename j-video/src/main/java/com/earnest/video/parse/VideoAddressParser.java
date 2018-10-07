package com.earnest.video.parse;

import com.earnest.video.entity.BaseVideoEntity;

@FunctionalInterface
public interface VideoAddressParser<T extends BaseVideoEntity> {

    String parse(T t);
}
