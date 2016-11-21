package com.zxcc.testThread;

/**
 * 检查信息不一致的 task
 */
public class CheckForInconsistentTask implements Runnable{
    private User user;

    public CheckForInconsistentTask(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        while(true) {
            synchronized (user) {
                if(user.getUserId1() != user.getUserId2()) {
                    System.out.println("产生了不一致的情况: " + user);
                }
            }
        }
    }
}
