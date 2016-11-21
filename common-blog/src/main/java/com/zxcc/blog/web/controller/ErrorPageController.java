package com.zxcc.blog.web.controller;

import com.zxcc.blog.article.dto.ArticleDtoBrief;
import com.zxcc.blog.article.service.ArticleService;
import com.zxcc.blog.base.dto.PageInfo;
import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.service.CategoryService;
import com.zxcc.blog.user.constant.UserConstant;
import com.zxcc.blog.user.entity.User;
import com.zxcc.blog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 错误页面controller
 * Created by xuanzh.cc on 2016/8/14.
 */
@Controller
public class ErrorPageController {


    @RequestMapping("/err/400")
    public String error400(){
        return "error/400";
    }

    @RequestMapping("/err/404")
    public String error404(){
        return "error/404";
    }

    @RequestMapping("/err/500")
    public String error500(){
        return "error/500";
    }
}
