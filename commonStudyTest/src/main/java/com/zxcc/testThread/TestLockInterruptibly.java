package com.zxcc.testThread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuanzh.cc on 2016/9/20.
 */
public class TestLockInterruptibly {
    private static ReentrantLock lock1 = new ReentrantLock();
    private static ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new TestThread(true), "线程1");
        Thread t2 = new Thread(new TestThread(false), "线程2");

        t1.start();

        Thread.sleep(50);

        t2.start();

        //主线程休眠3秒
        Thread.sleep(3000);
        //t2发送中断，让等待lock1的t2响应该中断，取消对lock1的请求
        t2.interrupt();
    }

    static class TestThread implements Runnable{
        //构造
        public TestThread(boolean flag) {
            this.flag = flag;
        }
        //true 先获取 lock1，再获取lock2
        //false 先获取 lock2，再获取lock1
        private boolean flag;
        @Override
        public void run() {

            try{
                if(flag) {
                    System.out.println(Thread.currentThread().getName() + " 开始获取lock1 ");
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 获取到了 lock1");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 开始获取lock2 ");
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 获取到了 lock2");
                } else {
                    System.out.println(Thread.currentThread().getName() + " 开始获取lock2 ");
                    lock2.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 获取到了 lock2");

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + " 开始获取lock1 ");
                    lock1.lockInterruptibly();
                    System.out.println(Thread.currentThread().getName() + " 获取到了 lock1");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                if(lock2.isHeldByCurrentThread()){
                    lock2.unlock();
                }

                System.out.println(Thread.currentThread().getName() + "  " + "end...");
            }
        }
    }
}
