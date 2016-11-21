package com.zxcc.testThread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.*;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestThreadPool {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.submit(new ParseTask("ww222"));
        pool.submit(new ParseTask("23243"));
        pool.submit(new ParseTask("234"));
        pool.submit(new ParseTask("32423"));

/*        ExecutorService pool2 = new TraceThreadPoolExecutor(2, 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
        pool2.submit(new ParseTask("xxx1"));
        pool2.submit(new ParseTask("55"));
        pool2.submit(new ParseTask("88"));
        pool2.submit(new ParseTask("99"));*/



    }

    static class ParseTask implements Runnable{
        private String str;

        public ParseTask(String str) {
            this.str = str;
        }

        @Override
        public void run() {
            System.out.println(Integer.parseInt(str));
        }
    }

    static class TraceThreadPoolExecutor extends ThreadPoolExecutor{

        public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }


        @Override
        public void execute(Runnable command) {
            super.execute(wrap(command, new Exception("exception stack trace"), Thread.currentThread().getName()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(wrap(task, new Exception("exception stack trace"), Thread.currentThread().getName()));
        }

        private Runnable wrap(final Runnable command, final Exception clientStack, String threadName){
            return new Runnable() {
                @Override
                public void run() {
                    try{
                        command.run();
                    }catch (Exception e){
                        clientStack.printStackTrace();
                        throw e;
                    }
                }
            };
        }
    }
}


