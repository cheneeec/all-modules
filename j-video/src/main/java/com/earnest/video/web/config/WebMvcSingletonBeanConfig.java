package com.earnest.video.web.config;

import com.earnest.video.entity.Video;
import com.earnest.video.repository.VideoRepository;
import com.earnest.video.service.MemoryVideoService;
import com.earnest.video.service.MongoVideoService;
import com.earnest.video.service.VideoService;
import com.earnest.video.spider.persistence.MemoryVideoPersistence;
import com.earnest.video.spider.persistence.VideoPersistence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class WebMvcSingletonBeanConfig {

    //=======================Video Service========================
    @Bean
    @Profile("mongo")
    public VideoService mongoVideoService(VideoRepository videoRepository) {
        return new MongoVideoService(videoRepository);
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("unchecked")
    public VideoService memoryVideoService(VideoPersistence memoryVideoPersistence) {
        return new MemoryVideoService((MemoryVideoPersistence<Video>) memoryVideoPersistence);
    }
    //=====================//Video Service========================
}
