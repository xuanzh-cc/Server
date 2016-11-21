package com.zxcc.testThread;

/**
 * Created by xuanzh.cc on 2016/9/19.
 */
public class TestStop {
    public static void main(String[] args) throws InterruptedException {
        User user = new User(1000);
        new Thread(new CheckForInconsistentTask(user)).start();
        while(true){
            Thread t = new Thread(new ChangeInfoTask(user));
            t.start();
            t.sleep(30);
            t.stop();
        }
    }
}
