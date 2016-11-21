package com.zxcc.blog.picture.service;

import com.zxcc.blog.picture.constant.ImageUploadTypeEnum;
import com.zxcc.blog.picture.dto.PictureUploadResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/8/21.
 */
public interface PictureUploadService {

    /**
     * 上传图片
     * @param servletContext
     * @param fileKey
     * @param imageUploadTypeEnum
     * @return
     * @throws IOException
     */
    public PictureUploadResult upload(ServletContext servletContext, MultipartFile fileKey, ImageUploadTypeEnum imageUploadTypeEnum) throws IOException;
}
