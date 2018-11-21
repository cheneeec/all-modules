package com.earnest.video.spider.persistence;

import com.earnest.video.entity.Video;
import com.earnest.video.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;


@AllArgsConstructor
public class MongoDBVideoPersistence implements VideoPersistence {

    private final VideoRepository videoRepository;


    @Override
    public void save(List<? extends Video> entities) {
        if (!CollectionUtils.isEmpty(entities)) {
            videoRepository.saveAll(entities);
        }
    }


    @Override
    public long count() {
        return videoRepository.count();
    }
}
