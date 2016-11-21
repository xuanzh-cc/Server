package com.zxcc.blog.picture.controller;

import com.zxcc.blog.picture.constant.ImageUploadTypeEnum;
import com.zxcc.blog.picture.dto.PictureUploadResult;
import com.zxcc.blog.picture.service.PictureUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by xuanzh.cc on 2016/8/21.
 */
@Controller
@RequestMapping("/picture")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Autowired
    private PictureUploadService pictureUploadService;

    /**
     * 图片上传
     * @return
     */
    @RequestMapping(value = "/admin/upload", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public PictureUploadResult picutreUpload(HttpServletRequest request, MultipartFile upload_file, Integer uploadType){
        ServletContext servletContext = request.getSession().getServletContext();
        try{
            ImageUploadTypeEnum imageUploadTypeEnum = ImageUploadTypeEnum.getById(uploadType);

            return pictureUploadService.upload(servletContext, upload_file, imageUploadTypeEnum);

        } catch (NullPointerException e) {
            return new PictureUploadResult(false, "上传类型不能为空！");
        } catch (NumberFormatException e) {
            return new PictureUploadResult(false, "上传类型不正确！");
        } catch (Exception e) {
            logger.error("", e);
            return new PictureUploadResult(false, "上传失败！");
        }
    }
}
