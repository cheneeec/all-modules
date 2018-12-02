package com.earnest.video.parser;

import com.earnest.video.exception.ValueParseException;
import com.earnest.video.entity.Episode;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.Map;

public interface VideoAddressParser {
    /**
     * 对提交的地址进行解析。
     *
     * @param rawValue   　原地址。
     * @param properties 　额外的属性。
     * @throws IOException         在处理发生IO相关的异常。
     * @throws ValueParseException 　通常时由于解析的过程内部发生的错误。
     * @return　 {@link Episode}
     */
    Episode parse(@NonNull String rawValue, @Nullable Map<String, Object> properties) throws IOException, ValueParseException;

    default boolean support(@NonNull String rawValue) {
        return true;
    }

    /**
     * 设置解析器的优先级别。
     *
     * @return　解析器的优先级别。
     */
    default int priority() {
        return Integer.MAX_VALUE;
    }

}
