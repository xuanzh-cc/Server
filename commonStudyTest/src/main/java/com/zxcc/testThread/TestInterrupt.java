package com.zxcc.testThread;

/**
 * Created by xuanzh.cc on 2016/9/19.
 */
public class TestInterrupt {
    public static void main(String[] args) throws InterruptedException {
        ThreadTest t = new ThreadTest();
        t.start();

        Thread.sleep(5000);

        t.join();

    }

}

class ThreadInterruptTest extends Thread{
    /* 运行标记 */
    private volatile boolean canRun = true;

    @Override
    public void run() {
        while(true) {
            //如果当前线程被设置为中断
            if(Thread.currentThread().isInterrupted()){
                System.out.println("线程被中断......");
                break ;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

            //这里是其他的逻辑
        }
    }
}
