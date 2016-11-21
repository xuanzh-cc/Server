package com.zxcc.blog.user.entity;

import java.io.Serializable;

/**
 * 博客用户
 * Created by xuanzh.cc on 2016/8/4.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 422896873846710809L;

    /** 用户主键ID */
    private long userId;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 个人信息 */
    private String profile;
    /** 别名 */
    private String nickname;
    /** 个性签名 */
    private String sign;
    /** 头像地址 */
    private String imageUrl;


    public static User valueOf(long userId) {
        User user = new User();
        user.userId = userId;
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", profile='" + profile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sign='" + sign + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
