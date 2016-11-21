package com.zxcc.testThread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuanzh.cc on 2016/9/20.
 */
public class TestCondition {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition produceGood = lock.newCondition();
    private static Condition consumeGood = lock.newCondition();
    /* 是否有物品 */
    private static boolean hasGood = false;

    public static void main(String[] args) {
        Thread producer = new Thread(new Producer(),"生产者");
        Thread consumer = new Thread(new Consumer(),"消费者");

        producer.start();
        consumer.start();

    }

    /**生产者*/
    static class Producer implements Runnable {
        @Override
        public void run() {
            while(true) {
                try{
                    lock.lock();
                    //如果有生产
                    if (hasGood) {
                        System.out.println("线程 " + Thread.currentThread().getName() + " 物品等待被消费..");
                        try {
                            //等待物品被消费后，被消费者唤醒
                            consumeGood.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //没有物品，生产者则生产物品
                    else {
                        System.out.println("线程 " + Thread.currentThread().getName() + " 生产了一个物品...");
                        hasGood = true;
                        //唤醒正在等待的消费者开始消费
                        produceGood.signal();
                    }
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /*消费者*/
    static class Consumer implements Runnable{
        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    //如果有物品
                    if(hasGood) {
                        //消费物品
                        System.out.println("线程 " + Thread.currentThread().getName() + " 消费了物品...");
                        hasGood = false;
                        //唤醒等待生产者开始生产
                        consumeGood.signal();
                    }
                    //没有物品
                    else {
                        System.out.println("线程 " + Thread.currentThread().getName() + "等待物品被生产..");
                        try {
                            //等待被生产者唤醒
                            produceGood.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    lock.unlock();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
