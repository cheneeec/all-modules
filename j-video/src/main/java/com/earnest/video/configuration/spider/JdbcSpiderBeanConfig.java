package com.earnest.video.configuration.spider;

import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.entity.Video;
import com.earnest.video.repository.VideoRepository;
import com.earnest.video.service.BasicQueryAndPersistenceVideoService;
import com.earnest.video.service.JdbcVideoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 当环境为jdbc时，保存到数据库
 */
@Profile("jdbc")
@Configuration
public class JdbcSpiderBeanConfig {

    @Bean
    public BasicQueryAndPersistenceVideoService<Video> basicQueryAndPersistenceVideoService(VideoRepository videoRepository) {
        return new JdbcVideoService(videoRepository);
    }

    @Bean
    public Consumer<List<BaseVideoEntity>> videoEntitiesConsumer(BasicQueryAndPersistenceVideoService<Video> persistenceVideoService) {
        return entities -> {
            List<Video> videos = entities.stream()
                    .map(Video::adapt)
                    .collect(Collectors.toList());
            persistenceVideoService.save(videos);

        };

    }
}
