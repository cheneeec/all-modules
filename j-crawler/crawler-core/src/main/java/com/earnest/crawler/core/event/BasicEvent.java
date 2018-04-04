package com.earnest.crawler.core.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.EventObject;

public abstract class BasicEvent extends EventObject {

    @Getter
    private final LocalDateTime time;


    public BasicEvent(Object source) {
        super(source);
        time = LocalDateTime.now();
    }
}
