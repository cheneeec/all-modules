package com.earnest.video.spider.persistence;

import com.earnest.video.entity.VideoEntity;

import java.util.List;

public interface VideoPersistence<T extends VideoEntity> {

    void save(List<T> entities);

    long count();
}
