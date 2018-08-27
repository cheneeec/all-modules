package com.earnest.crawler.core.configurer;

/**
 * 指向上级。
 *
 * @param <T>
 */
@FunctionalInterface
public interface SuperiorReferenceConfigurer<T extends Configurer> extends Configurer {
    T and();
}
