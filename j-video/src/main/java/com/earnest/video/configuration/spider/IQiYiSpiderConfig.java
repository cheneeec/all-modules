package com.earnest.video.configuration.spider;

import com.earnest.video.entity.BaseVideoEntity;
import com.earnest.video.service.BasicQueryAndPersistenceVideoService;
import com.earnest.video.spider.IQiYiAnimationSpider;
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
    public IQiYiAnimationSpider iQiYiAnimationSpider(BasicQueryAndPersistenceVideoService basicQueryAndPersistenceVideoService, @Autowired(required = false) Consumer<List<BaseVideoEntity>> videoEntitiesConsumer) {

        return new IQiYiAnimationSpider(basicQueryAndPersistenceVideoService) {
            @Override
            protected Consumer<List<BaseVideoEntity>> consumer() {
                return Optional.ofNullable(videoEntitiesConsumer).orElse(super.consumer());
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicQueryAndPersistenceVideoService basicQueryAndPersistenceVideoService(){
        return null;
    }


}
