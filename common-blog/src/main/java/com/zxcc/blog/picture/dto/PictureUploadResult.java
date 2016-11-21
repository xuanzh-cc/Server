package com.zxcc.blog.picture.dto;

import com.zxcc.blog.base.dto.BaseOperationResult;

/**
 * 图片上传结果
 * Created by xuanzh.cc on 2016/8/21.
 */
public class PictureUploadResult extends BaseOperationResult{

    //上传后的文件路径
    private String file_path;

    public PictureUploadResult(boolean success, String msg) {
        super();
        this.success = success;
        this.msg = msg;
    }

    public PictureUploadResult(boolean success, String msg, String file_path) {
        this(success, msg);
        this.file_path = file_path;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
