package com.earnest.example;

import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {

    private final StampedLock lock = new StampedLock();

    private int balance;

    public void read() {
        long stamp = lock.readLock();
        int c = balance;
        System.out.println("balance：" + balance);
        lock.unlock(stamp);
    }

    public void conditionReadWrite(int value) {
        //首先判断balance的值是否符合更新的条件
        long stamp = lock.readLock();
        while (balance > 0) {
            long writeStamp = lock.tryConvertToWriteLock(stamp);
            //成功转化为写锁
            if (writeStamp != 0) {
                stamp = writeStamp;
                balance += value;
                break;
            } else {
                //没有转化成写锁，这里需要首先释放读锁，然后再拿到写锁
                lock.unlockRead(stamp);
                //
                stamp = lock.writeLock();

            }
        }
        lock.unlock(stamp);
    }

    public void optimisticRead() {
        long stamp = lock.tryOptimisticRead();
        int c = balance;
        //这里可能会出现写操作
        if (!lock.validate(stamp)) {
            //校验不通过重新读
            long readStamp = lock.readLock();
            c = balance;
            stamp = readStamp;
        }
        //
        lock.unlockRead(stamp);
    }

    public void write(int value) {
        long stamp = lock.writeLock();
        balance += value;
        lock.unlock(stamp);
    }

    public static void main(String[] args) {
        StampedLockExample stampedLockExample = new StampedLockExample();

    }
}
