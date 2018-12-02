package com.earnest.video;

@FunctionalInterface
public interface Manager<E> {
    void addWork(E e);
}
