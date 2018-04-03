package com.earnest.core.scheduler;


import com.earnest.crawler.core.request.HttpGetRequest;
import com.earnest.crawler.core.scheduler.BlockingUniqueScheduler;
import com.earnest.crawler.core.scheduler.Scheduler;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class BlockingLinkedHashSetSchedulerTest {

    public static void main(String[] args) {
        Scheduler scheduler = new BlockingUniqueScheduler();
        Thread t1 = new Thread(() -> {
          /*  scheduler.put(new HttpGetRequest("1"));
            System.out.println(name() + "已经放入数据：1");*/
            System.out.println("开始");
            System.out.println(name() + scheduler.take().getUrl());
            System.out.println(name() + scheduler.take().getUrl());
            System.out.println(name() + scheduler.take().getUrl());
        }, "t1");

        Thread t2 = new Thread(() -> {
            sleep(3);
            System.out.println("3秒过后");
            scheduler.put(new HttpGetRequest("2"));
            scheduler.put(new HttpGetRequest("3"));
            System.out.println(name() + "已经放入：2,3");
            System.out.println(name() + scheduler.take().getUrl());
            System.out.println(name() + scheduler.take().getUrl());
            System.out.println(name() + scheduler.take().getUrl());
        }, "t2");

        Thread t3 = new Thread(() -> {
            sleep(6);
            System.out.println("6秒过后");
            scheduler.addAll(Arrays.asList(new HttpGetRequest("4"), new HttpGetRequest("5"), new HttpGetRequest("6"), new HttpGetRequest("7")));
            System.out.println(name() + "放入数据4,5,6");
        }, "t3");

        t1.start();
        t2.start();
        t3.start();
    }

    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String name() {
        return Thread.currentThread().getName() + "取得数据->";
    }
}