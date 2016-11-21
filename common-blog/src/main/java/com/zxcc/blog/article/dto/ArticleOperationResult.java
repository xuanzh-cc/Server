package com.zxcc.blog.article.dto;

import com.zxcc.blog.base.dto.BaseOperationResult;

/**
 * 文章模块 操作结果
 * Created by xuanzh.cc on 2016/8/21.
 */
public class ArticleOperationResult<T> extends BaseOperationResult {
    //数据
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
