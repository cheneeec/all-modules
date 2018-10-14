package com.earnest.video.config.spider;

import com.earnest.video.entity.VideoEntity;
import com.earnest.video.entity.Video;
import com.earnest.video.repository.VideoRepository;
import com.earnest.video.service.BasicQueryAndPersistenceVideoService;
import com.earnest.video.service.MongodbVideoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 当环境为mongodb时，连接到对应数据库。
 */
@Profile("mongo")
@Configuration
public class MongoSpiderBeanConfig {

    @Bean
    public BasicQueryAndPersistenceVideoService<Video> basicQueryAndPersistenceVideoService(VideoRepository videoRepository) {
        return new MongodbVideoService(videoRepository);
    }

    @Bean
    public Consumer<List<VideoEntity>> videoEntitiesConsumer(BasicQueryAndPersistenceVideoService<Video> persistenceVideoService) {
        return entities -> {
            List<Video> videos = entities.stream()
                    .map(Video::adapt)
                    .collect(Collectors.toList());
            persistenceVideoService.save(videos);

        };

    }
}
