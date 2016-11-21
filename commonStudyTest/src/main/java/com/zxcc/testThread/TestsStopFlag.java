package com.zxcc.testThread;

/**
 * Created by xuanzh.cc on 2016/9/19.
 */
public class TestsStopFlag {

    public static void main(String[] args) throws InterruptedException {
        ThreadTest t = new ThreadTest();
        t.start();

        Thread.currentThread().sleep(3000);
        t.stopMe();
    }
}

class ThreadTest extends Thread{
    /* 运行标记 */
    private volatile boolean canRun = true;

    @Override
    public void run() {
        while(true) {
            if(!canRun){
                System.out.println("线程被标记为不能运行，即将停止！");
                break ;
            } else {
                System.out.println("running ...... ");
                try {
                    this.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置标记为false
     */
    public void stopMe() {
        this.canRun = false;
    }
}


