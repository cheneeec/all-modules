package com.earnest.web.autoconfigure.spider.mongo;

import com.earnest.web.repository.VideoRepository;
import com.earnest.web.service.MongoVideoService;
import com.earnest.web.service.VideoService;
import com.earnest.web.spider.persistence.MongoDBVideoPersistence;
import com.earnest.web.spider.persistence.VideoPersistence;
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
