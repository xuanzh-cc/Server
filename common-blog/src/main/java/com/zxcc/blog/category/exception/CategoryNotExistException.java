package com.zxcc.blog.category.exception;

/**
 * 类别不存在异常
 * Created by xuanzh.cc on 2016/9/12.
 */
public class CategoryNotExistException extends RuntimeException {
    public CategoryNotExistException(String message) {
        super(message);
    }

    public CategoryNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
