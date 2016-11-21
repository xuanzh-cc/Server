package com.zxcc.testThread;

/**
 * Created by xuanzh.cc on 2016/9/19.
 */
public class User {
    /* 用户ID1 */
    private int userId1;
    /* 用户ID2 */
    private int userId2;

    /**
     * constructor。。
     * 为了便于演示,这里将两个id设置为一样
     * @param userId
     */
    public User(int userId) {
        this.userId1 = userId;
        this.userId2 = userId;
    }

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId1=" + userId1 +
                ", userId2=" + userId2 +
                '}';
    }
}
