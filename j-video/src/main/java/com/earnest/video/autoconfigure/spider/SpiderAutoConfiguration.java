package com.earnest.video.autoconfigure.spider;

import com.earnest.video.service.MemoryVideoService;
import com.earnest.video.service.VideoService;
import com.earnest.video.spider.persistence.VideoPersistence;
import com.earnest.video.spider.persistence.MemoryVideoPersistence;
import com.earnest.video.spider.IQiYiAnimationSpider;
import com.earnest.video.spider.IQiYiMovieSpider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * iQiYi的爬虫配置
 */
@Configuration

public class SpiderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public VideoPersistence videoPersistence() {
        return new MemoryVideoPersistence();
    }

    @Bean
    @ConditionalOnMissingBean
    @SuppressWarnings("unchecked")
    public VideoService videoService(VideoPersistence memoryVideoPersistence) {
        return new MemoryVideoService((MemoryVideoPersistence) memoryVideoPersistence);
    }


    @ConditionalOnProperty(name = "spider.enable", havingValue = "true")
    @Configuration
    public class SpiderBeanAutoConfiguration {
        @Bean
        public IQiYiAnimationSpider iQiYiAnimationSpider(VideoPersistence videoPersistence) {
            return new IQiYiAnimationSpider(videoPersistence);
        }

        @Bean
        public IQiYiMovieSpider iQiYiMovieSpider(VideoPersistence videoPersistence) {
            return new IQiYiMovieSpider(videoPersistence);
        }
    }


}
