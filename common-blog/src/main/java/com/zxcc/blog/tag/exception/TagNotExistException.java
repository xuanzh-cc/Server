package com.zxcc.blog.tag.exception;

/**
 * 类别不存在异常
 * Created by xuanzh.cc on 2016/9/12.
 */
public class TagNotExistException extends RuntimeException {
    public TagNotExistException(String message) {
        super(message);
    }

    public TagNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
