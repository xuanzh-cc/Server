package com.zxcc.testThread;

/**
 * Created by xuanzh.cc on 2016/9/20.
 */
public class TestJoin {
    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(){
            @Override
            public void run() {
                for(int i = 1; i <= 10; i++) {
                    System.out.println("第" + i + "次输出...");
                }
            }
        };

        t.start();

        t.join();

        System.out.println("main 线程输出...");
    }
}
