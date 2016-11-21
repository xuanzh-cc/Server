package com.zxcc.jstack;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuanzh.cc on 2016/9/29.
 */
public class TestJstack {

    private static Object LOCK = new Object();

    private static Object WAIT_OBJECT = new Object();

    private static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        //运行状态
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    while (true) {
                        int i = 0;
                        i = i + 1;
                    }
                }
            }
        }, "state - RUNNABLE_held_lock");

        //等待 LOCK 锁
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (LOCK) {
                    System.out.println("拿到了锁");
                }
            }
        }, "state - WAITING_for_lock");

        //等待被监视器 WAIT_OBJECT 唤醒
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (WAIT_OBJECT) {
                    System.out.println("获取 WAIT_OBJECT 后释放");
                    try {
                        WAIT_OBJECT.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "state - WAIT__WAIT_OBJECT.wait()");

        //休眠
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //休眠两分钟
                    Thread.sleep(TimeUnit.MINUTES.toMillis(2));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "state - TIME_WAIT_sleep");


        Thread t5 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    while (true) {
                        int i = 0;
                        i = i + 1;
                    }
                } finally {
                    reentrantLock.unlock();
                }
            }
        }, "state - RUNNABLE_held_reentrantLock");

        //等待 reentrantLock 锁
        Thread t6 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    System.out.println("拿到了锁 reentrantLock");
                } finally {
                    reentrantLock.unlock();
                }
            }
        }, "state - WAITING_for_reentrantLock");

        t1.start();
        Thread.sleep(100);
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        Thread.sleep(100);
        t6.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
    }
}
