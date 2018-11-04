package com.earnest.video.autoconfigure.spider;

import com.earnest.video.entity.Video;
import com.earnest.video.entity.VideoEntity;
import com.earnest.video.service.MemoryVideoService;
import com.earnest.video.service.VideoService;
import com.earnest.video.spider.persistence.VideoPersistence;
import com.earnest.video.spider.persistence.MemoryVideoPersistence;
import com.earnest.video.spider.IQiYiAnimationSpider;
import com.earnest.video.spider.IQiYiMovieSpider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * iQiYi的爬虫配置
 */
@Configuration
public class SpiderAutoConfiguration {

    @Bean
    public IQiYiAnimationSpider iQiYiAnimationSpider(VideoPersistence videoPersistence, @Autowired(required = false) Consumer<List<? extends VideoEntity>> videoEntitiesConsumer) {

        return new IQiYiAnimationSpider(videoPersistence) {
            @Override
            protected Consumer<List<? extends VideoEntity>> consumer() {
                return Optional.ofNullable(videoEntitiesConsumer).orElse(super.consumer());
            }


        };
    }

    @Bean
    public IQiYiMovieSpider iQiYiMovieSpider(VideoPersistence videoPersistence,
                                             @Autowired(required = false) Consumer<List<? extends VideoEntity>> videoEntitiesConsumer) {

        return new IQiYiMovieSpider(videoPersistence) {
            @Override
            protected Consumer<List<? extends VideoEntity>> consumer() {
                return Optional.ofNullable(videoEntitiesConsumer).orElse(super.consumer());
            }
        };
    }


    @Bean
    @ConditionalOnMissingBean
    public VideoPersistence<Video> videoPersistence() {
        return new MemoryVideoPersistence<>();
    }


    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("unchecked")
    public VideoService videoService(VideoPersistence memoryVideoPersistence) {
        return new MemoryVideoService((MemoryVideoPersistence<Video>) memoryVideoPersistence);
    }

}
