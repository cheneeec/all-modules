package com.earnest.crawler.core.builder;


public interface Configurer{

    /**
     * 配置器的初始化。
     *
     * @throws Exception 忽略任何异常。
     */
    void init() throws Exception;

    /**
     * 对B进行配置。
     *
     * @throws Exception 忽略任何异常。
     */
    void configure() throws Exception;

}
