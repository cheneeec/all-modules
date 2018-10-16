package com.earnest.crawler.configurer;

/**
 * 指向上级。
 *
 * @param <T>
 */
@FunctionalInterface
public interface SuperiorReferenceConfigurer<T extends Configurer> extends Configurer {
    T and();
}
