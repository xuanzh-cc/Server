package com.zxcc.blog.picture.service.impl;

import com.zxcc.blog.picture.constant.ImageUploadTypeEnum;
import com.zxcc.blog.picture.dto.PictureUploadResult;
import com.zxcc.blog.picture.service.PictureUploadService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/8/21.
 */
@Component
public class PictureUploadServiceImpl implements PictureUploadService {

    //文章图片上传所用的目录
    @Value("${web.articleUploadImageDir}")
    private String articleImageDir;

    @PostConstruct
    public void init(){
        ImageUploadTypeEnum.common.setImageDirPath(articleImageDir);
        ImageUploadTypeEnum.articleImage.setImageDirPath(articleImageDir);
        ImageUploadTypeEnum.userImage.setImageDirPath(articleImageDir);
        ImageUploadTypeEnum.selfPhoto.setImageDirPath(articleImageDir);

    }


    @Override
    public PictureUploadResult upload(ServletContext servletContext, MultipartFile uploadFile, ImageUploadTypeEnum imageUploadTypeEnum) throws IOException{
        String path = imageUploadTypeEnum.getImageDirPath();
        String dirPath = servletContext.getRealPath(path);
        String fileName = this.genFileName(uploadFile.getOriginalFilename(), imageUploadTypeEnum.name());
        File file = new File(dirPath, fileName);
        if(!file.exists()) {
            FileUtils.touch(file);
        }
        uploadFile.transferTo(file);
        return new PictureUploadResult(true, "文件上传成功！", this.getFileRealPath(servletContext, imageUploadTypeEnum, fileName));
    }

    /**
     * 生成文件名称
     * @param fileName
     * @param typeName
     * @return
     */
    private String genFileName(String fileName, String typeName){
        long current = System.currentTimeMillis();
        StringBuilder builder = new StringBuilder();
        return builder.append(typeName).append("_").append(current).append("_").append(fileName).toString();
    }

    /**
     * 获取图片在web中的路径
     * @param imageUploadTypeEnum
     * @param fileName
     * @return
     */
    private String getFileRealPath(ServletContext servletContext, ImageUploadTypeEnum imageUploadTypeEnum, String fileName){
        return servletContext.getContextPath() + imageUploadTypeEnum.getImageDirPath() + fileName;
    }
}
