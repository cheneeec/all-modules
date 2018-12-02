package com.earnest.web.spider.persistence;

import com.earnest.video.entity.Video;

import java.util.List;

public interface VideoPersistence {

    void save(List<? extends Video> entities);

    long count();
}
