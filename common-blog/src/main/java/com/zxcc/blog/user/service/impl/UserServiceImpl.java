package com.zxcc.blog.user.service.impl;

import com.zxcc.blog.user.constant.UserConstant;
import com.zxcc.blog.user.constant.UserOperationMessage;
import com.zxcc.blog.user.dao.UserDao;
import com.zxcc.blog.user.entity.User;
import com.zxcc.blog.user.exception.UserNotExistException;
import com.zxcc.blog.user.exception.UserNotLoginException;
import com.zxcc.blog.user.exception.UserPasswordNotMatchException;
import com.zxcc.blog.user.service.UserService;
import com.zxcc.blog.web.utils.HttpSessionUtils;
import com.zxcc.utility.CryptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * Created by xuanzh.cc on 2016/8/4.
 */
@Component
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /** 盐值 */
    private static final String salt = "c2&T*YUG(*&*(IUJ5454y57655443567*&^*";

    @Autowired
    private UserDao userDao;

    @Override
    public User getByUsername(String username) {
        User user = this.userDao.queryByUsername(username);
        return user;
    }

    @Override
    public void passwordModify(long userId, String password, String oldPassword) throws UserPasswordNotMatchException{
        User user = this.getByUserId(userId);
        String oldPasswordMd5 = this.getMd5EncodingPassword(oldPassword);

        //密码不匹配
        if(!user.getPassword().equals(oldPasswordMd5)){
            throw new UserPasswordNotMatchException(UserOperationMessage.PASSWORD_NOT_MATCH);
        }

        String inputPassword = this.getMd5EncodingPassword(password);
        userDao.updateUserPassword(userId, inputPassword);
    }

    @Override
    public User validLogin(String username, String password) {
        User user = this.getByUsername(username);

        if(user == null) {
            return null;
        }
        String inputPassword = this.getMd5EncodingPassword(password);

        return (inputPassword != null && inputPassword.equals(user.getPassword())) ? user : null;
    }

    @Override
    public User getByUserId(long userId) {
        return this.userDao.queryById(userId);
    }

    /**
     * 获得密码的 md5 加密串
     * @param password
     * @return
     */
    private String getMd5EncodingPassword(String password){
        try {
            return CryptUtils.md5(password + salt);
        } catch (Exception e) {
            logger.error("MD5 编码失败！", e);
        }
        return null;
    }


    public static void main(String[] args) {
        System.out.println(new UserServiceImpl().getMd5EncodingPassword("123456"));
    }
}
