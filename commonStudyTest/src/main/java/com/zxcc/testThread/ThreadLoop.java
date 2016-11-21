package com.zxcc.testThread;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuanzh.cc on 2016/9/28.
 */
public class ThreadLoop {
    /** 执行30秒钟 */
    private static final long DURATION = TimeUnit.SECONDS.toMillis(30);
    //long duration = TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS);

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread t1 = new TestThread("testThread-1");
        Thread t2 = new TestThread("testThread-2");
        Thread t3 = new TestThread("testThread-3");

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

        long end = System.currentTimeMillis();
        System.out.println("总共执行了：" + (end - start) + " ms...");

    }

    static class TestThread extends  Thread{

        public TestThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            while(true) {
                if (System.currentTimeMillis() - start >= DURATION) {
                    System.out.println("线程" + this.getName() + "退出循环...");
                    return ;
                }
                System.out.println(this.getName() + ":  " + System.currentTimeMillis());
            }
        }
    }
}
