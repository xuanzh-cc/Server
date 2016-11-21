package com.zxcc.blog.user.dto;

import com.zxcc.blog.base.dto.BaseOperationResult;

import java.util.Map;

/**
 * Created by xuanzh.cc on 2016/8/4.
 */
public class UserOperationResult<T> extends BaseOperationResult {

    /** 返回数据 */
    private T data;

    public UserOperationResult(boolean success){
        this.success = success;
    }

    public UserOperationResult(boolean success, String msg){
        this.success = success;
        this.msg = msg;
    }

    public UserOperationResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
