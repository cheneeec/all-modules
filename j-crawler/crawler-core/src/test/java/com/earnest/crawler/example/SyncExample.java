package com.earnest.crawler.example;

import java.util.concurrent.TimeUnit;

public class SyncExample {

    public synchronized void show()  {
        System.out.println(Thread.currentThread().getName()+"进入show()。。。");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"离开show()。。。");
    }


    public static synchronized void show1()  {
        System.out.println(Thread.currentThread().getName()+"进入show2()。。。");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"离开show2()。。。");
    }
    public static void main(String[] args) throws Exception {
        SyncExample example=new SyncExample();
        Thread t1=new Thread(SyncExample::show1,"t1");
        Thread t2=new Thread(SyncExample::show1,"t2");
        t1.start();
        t2.start();
    }
}
