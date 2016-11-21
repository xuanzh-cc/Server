package com.zxcc.engine.test;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */
public class Engine {

    private Worker[] workers ;

    public void init(){
        workers = new Worker[Runtime.getRuntime().availableProcessors() * 2];
        for (int i = 0; i < workers.length; i++) {
            Worker worker = new Worker();
            workers[i] = worker;
            new Thread(worker, "工作线程 - " + i).start();
        }
    }

    public void execute(Task task) {
        int index = task.getId() % workers.length;
        workers[index].addTask(task);
    }

    public static void main(String[] args) {

        Engine engine = new Engine();
        engine.init();

        for(int i = 0; i <= 500; i++){
            engine.execute(new Task(i, 0, -1) {
                @Override
                protected void doAction() {
                    System.out.println("正在执行任务" + this.getId());
                    System.out.println(Math.pow(2,5));
                    try {
                        Thread.currentThread().sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
