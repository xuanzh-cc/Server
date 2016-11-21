package com.zxcc.blog.article.exception;

/**
 * 文章不存在异常
 * Created by xuanzh.cc on 2016/9/12.
 */
public class ArticleNotExistException extends RuntimeException {
    public ArticleNotExistException(String message) {
        super(message);
    }

    public ArticleNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
