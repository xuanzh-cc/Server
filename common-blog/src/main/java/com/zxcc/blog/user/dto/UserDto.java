package com.zxcc.blog.user.dto;

/**
 * Created by xuanzh.cc on 2016/9/10.
 */
public class UserDto {
    private long userId;

    private String nickname;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
