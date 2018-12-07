package com.earnest.web.autoconfigure.spider;

import com.earnest.web.service.MemoryVideoService;
import com.earnest.web.service.VideoService;
import com.earnest.web.spider.IQiYiAnimationSpider;
import com.earnest.web.spider.IQiYiMovieSpider;
import com.earnest.web.spider.persistence.MemoryVideoPersistence;
import com.earnest.web.spider.persistence.VideoPersistence;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;


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
    public VideoService videoService(VideoPersistence memoryVideoPersistence) {
        return new MemoryVideoService((MemoryVideoPersistence) memoryVideoPersistence);
    }


    @ConditionalOnProperty(name = "app.spider.enable", havingValue = "true")
    @Configuration
    @AllArgsConstructor
    public class SpiderBeanAutoConfiguration {
        private final MongoTemplate mongoTemplate;

        @PostConstruct
        public void clearData() {
            //删除表
            mongoTemplate.dropCollection("video");
        }

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
