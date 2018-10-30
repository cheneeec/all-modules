package com.earnest.video.core;

@FunctionalInterface
public interface Manager<E> {
    void addWork(E e);
}
