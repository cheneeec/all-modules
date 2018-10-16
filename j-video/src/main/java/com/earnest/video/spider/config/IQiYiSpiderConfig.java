package com.earnest.video.spider.config;

import com.earnest.video.entity.VideoEntity;
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
public class IQiYiSpiderConfig {

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
    public VideoPersistence videoPersistenceService() {
        return new MemoryVideoPersistence();
    }


}
