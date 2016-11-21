package com.zxcc.blog.base.dto;

/**
 * 操作结果父类
 * Created by xuanzh.cc on 2016/8/21.
 */
public abstract class BaseOperationResult {
    /** 操作结果 */
    protected boolean success;

    /** 错误提示 */
    protected String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
