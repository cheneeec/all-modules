package com.earnest.crawler.core.worker;

import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.TimeUnit;

public interface IWorker {


    void pause();

    boolean isPause();



    void restart();


    default void interval(long seconds) {
        pause();
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        restart();
    }


    //间隔5到100秒后继续
    default void interval() {
        int randomInt = RandomUtils.nextInt(5, 100);
        System.out.println("暂停" + randomInt + "秒。。。");
        interval(randomInt);
    }


}
