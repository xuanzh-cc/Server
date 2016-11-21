package com.zxcc.blog.user.exception;

/**
 * 用户不存在异常
 * Created by xuanzh.cc on 2016/8/4.
 */
public class UserNotExistException extends RuntimeException{
    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
