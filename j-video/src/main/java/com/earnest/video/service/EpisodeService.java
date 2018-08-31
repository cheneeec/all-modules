package com.earnest.video.service;

import com.earnest.video.entity.Episode;

import java.util.List;

public interface EpisodeService {

    List<Episode> findAll(String url, int page, int size);
}
