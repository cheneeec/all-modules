package com.earnest.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionExample {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition =lock.newCondition();

    public void a() throws Exception {

        try {
            System.out.println("aaaa");
            condition.await();
            System.out.println("11111");

        } finally {

        }
    }

    public void b() throws Exception {

        try {
            System.out.println("b");
            TimeUnit.SECONDS.sleep(3);
            condition.signal();
            System.out.println("2222");
            TimeUnit.SECONDS.sleep(1);
        } finally {

        }
    }

    public static void main(String[] args) throws Exception {
        ConditionExample conditionExample = new ConditionExample();
        Thread t1 = new Thread(() -> {
            try {

                conditionExample.a();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                conditionExample.b();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }
}
