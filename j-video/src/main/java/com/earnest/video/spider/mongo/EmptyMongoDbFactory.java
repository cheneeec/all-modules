package com.earnest.video.spider.mongo;

import com.mongodb.DB;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.stereotype.Component;

/**
 * 主要防止{@link org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration}的自动装配。
 */
@Profile("default")
@Component
public class EmptyMongoDbFactory implements MongoDbFactory {

    @Override
    public MongoDatabase getDb() throws DataAccessException {
        return null;
    }

    @Override
    public MongoDatabase getDb(String dbName) throws DataAccessException {
        return null;
    }

    @Override
    public PersistenceExceptionTranslator getExceptionTranslator() {
        return null;
    }

    @Override
    public DB getLegacyDb() {
        return null;
    }
}
