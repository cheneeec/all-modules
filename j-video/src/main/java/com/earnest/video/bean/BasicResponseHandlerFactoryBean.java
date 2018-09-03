package com.earnest.video.bean;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.impl.client.BasicResponseHandler;
import org.springframework.beans.factory.FactoryBean;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicResponseHandlerFactoryBean implements FactoryBean<BasicResponseHandler> {

    private BasicResponseHandler responseHandler = new BasicResponseHandler();
    public static final BasicResponseHandlerFactoryBean INSTANCE = new BasicResponseHandlerFactoryBean();

    @Override
    public BasicResponseHandler getObject() {
        return responseHandler;
    }

    @Override
    public Class<?> getObjectType() {
        return BasicResponseHandler.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
