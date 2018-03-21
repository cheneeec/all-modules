package com.earnest.crawler.core.worker;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.TimeUnit;

public interface IWorker {

    /**
     * 暂停
     */
    void pause();

    boolean isPause();

    /**
     * 重新开始
     */
    void restart();

    /**
     * 在间隔指定秒数后重新开始
     *
     * @param seconds
     */
    default void interval(long seconds) {
        pause();
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        restart();
    }

    /**
     * 间隔5到100秒后继续
     */
    default void interval() {
        interval(RandomUtils.nextInt(5, 100));
    }



}
