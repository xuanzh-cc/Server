package com.zxcc.engine.test;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */
public class Worker implements Runnable{

    private volatile boolean running = true;

    private DelayQueue<Task> queue = new DelayQueue<Task>();

    public void addTask(Task task){
        this.queue.add(task);
    }

    @Override
    public void run() {
        while (running) {
            if (Thread.interrupted()) {
                //线程被中断...
                break;
            }

            try {
                Task task = this.queue.take();
                long startTime = System.currentTimeMillis();
                task.action();
                if (task.canNextAction()) {
                    this.queue.add(task);
                }
                long endTime = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName() + "  开始执行任务" + task.getId() + "  执行花费了" + (endTime - startTime) + "ms...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止
     */
    public void stop(){
        this.running = false;
    }
}
