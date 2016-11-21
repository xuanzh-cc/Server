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
 * Created by xuanzh.cc on 2016/8/14.
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/index")
    public ModelAndView index(){

        ModelAndView mdv = new ModelAndView();

        PageInfo pageInfo = this.articleService.getPageInfo(1, 5);

        List<ArticleDtoBrief> articles = this.articleService.getArticleBriefList(pageInfo.getOffset(), pageInfo.getLimit());

        List<CategoryDto> categories = this.categoryService.getCategoryList();

        mdv.addObject("pageInfo", pageInfo);
        mdv.addObject("articles", articles);
        mdv.addObject("categories", categories);

        mdv.setViewName("front/index" );

        return mdv;
    }

    /**
     * 后台首页
     * @return
     */
    @RequestMapping("/admin/index")
    public ModelAndView adminIndex(HttpSession session){
        ModelAndView mdv = new ModelAndView();
        Long userId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
        if(userId == null) {
            mdv.setViewName("backend/user/login/login");
            return mdv;
        }
        User user = this.userService.getByUserId(userId);
        mdv.addObject("user", user);
        mdv.setViewName("backend/index");
        return mdv;
    }

    /**
     * 后台首页 主页面部分
     * @return
     */
    @RequestMapping("/admin/main")
    public String adminMain(){
        return "backend/main";
    }


}
