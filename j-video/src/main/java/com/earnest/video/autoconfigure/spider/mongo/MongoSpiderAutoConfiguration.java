package com.earnest.video.autoconfigure.spider.mongo;

import com.earnest.video.repository.VideoRepository;
import com.earnest.video.service.MongoVideoService;
import com.earnest.video.service.VideoService;
import com.earnest.video.spider.persistence.VideoPersistence;
import com.earnest.video.spider.persistence.MongoDBVideoPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * 当环境为<code>mongo</code> 时，连接到对应数据库。
 */
@Profile("mongo")
@Configuration
public class MongoSpiderAutoConfiguration {

    @Bean
    public VideoPersistence videoPersistence(VideoRepository videoRepository) {
        return new MongoDBVideoPersistence(videoRepository);
    }

    @Bean
    public VideoService videoService(VideoRepository videoRepository) {
        return new MongoVideoService(videoRepository);
    }

}
