package com.zxcc.blog.category.dto;

import com.zxcc.blog.base.dto.BaseOperationResult;

/**
 * 类别模块 操作结果
 * Created by xuanzh.cc on 2016/8/21.
 */
public class CategoryOperationResult<T> extends BaseOperationResult {
    //数据
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
