package com.zxcc.blog.user.exception;

/**
 * 密码不匹配异常
 * Created by xuanzh.cc on 2016/9/16.
 */
public class UserPasswordNotMatchException extends RuntimeException{
    public UserPasswordNotMatchException(String message) {
        super(message);
    }

    public UserPasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
