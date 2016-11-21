package com.zxcc.blog.user.service;

import com.zxcc.blog.user.entity.User;
import com.zxcc.blog.user.exception.UserNotExistException;
import com.zxcc.blog.user.exception.UserNotLoginException;
import com.zxcc.blog.user.exception.UserPasswordNotMatchException;

/**
 * Created by xuanzh.cc on 2016/8/4.
 */
public interface UserService {

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @return
     */
    User getByUsername(String username);

    /**
     * 用户密码修改
     * @param userId
     * @param password
     * @param oldPassword
     * @return
     */
    void passwordModify(long userId, String password, String oldPassword) throws UserPasswordNotMatchException;

    /**
     * 验证用户登陆
     * @param username
     * @param password
     * @return
     */
    User validLogin(String username, String password);

    /**
     * 通过 userId 查询用户
     * @param userId
     * @return
     */
    User getByUserId(long userId);
}
