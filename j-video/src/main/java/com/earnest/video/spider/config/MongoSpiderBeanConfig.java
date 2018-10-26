package com.earnest.video.spider.config;

import com.earnest.video.entity.VideoEntity;
import com.earnest.video.entity.Video;
import com.earnest.video.repository.VideoRepository;
import com.earnest.video.service.MongoVideoService;
import com.earnest.video.service.VideoService;
import com.earnest.video.spider.persistence.VideoPersistence;
import com.earnest.video.spider.persistence.MongoDBVideoPersistence;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 当环境为<code>mongo</code> 时，连接到对应数据库。
 */
@Profile("mongo")
@Configuration
public class MongoSpiderBeanConfig {

    @Bean
    public VideoPersistence<Video> videoPersistence(VideoRepository videoRepository) {
        return new MongoDBVideoPersistence(videoRepository);
    }

    @Bean
    public Consumer<List<? extends VideoEntity>> videoEntitiesConsumer(VideoPersistence<Video> persistenceVideoService) {
        return entities -> {
            List<Video> videos = entities.stream()
                    .map(Video::adapt)
                    .collect(Collectors.toList());
            persistenceVideoService.save(videos);

        };

    }

    @Bean
    public VideoService mongoVideoService(VideoRepository videoRepository) {
        return new MongoVideoService(videoRepository);
    }
}
