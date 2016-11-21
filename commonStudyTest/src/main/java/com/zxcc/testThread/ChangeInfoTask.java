package com.zxcc.testThread;

import java.util.Random;

/**
 * 修改用户信息task
 */
public class ChangeInfoTask implements Runnable {
    private User user;
    private Random random = new Random(System.currentTimeMillis());

    public ChangeInfoTask(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        synchronized (user) {
            while(true) {
                int userId = random.nextInt(50000);
                user.setUserId1(userId);
                try {
                    Thread.currentThread().sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                user.setUserId2(userId);
            }
        }
    }
}
