package com.zxcc.blog.user.exception;

/**
 * 用户未登陆异常
 * Created by xuanzh.cc on 2016/8/4.
 */
public class UserNotLoginException extends RuntimeException{
    public UserNotLoginException(String message) {
        super(message);
    }

    public UserNotLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
