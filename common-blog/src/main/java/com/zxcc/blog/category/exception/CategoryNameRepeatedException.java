package com.zxcc.blog.category.exception;

/**
 * 类别名称重复异常
 * Created by xuanzh.cc on 2016/9/12.
 */
public class CategoryNameRepeatedException extends RuntimeException {
    public CategoryNameRepeatedException(String message) {
        super(message);
    }

    public CategoryNameRepeatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
