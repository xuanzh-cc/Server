package com.zxcc.blog.category.controller;

import com.zxcc.blog.article.dto.ArticleDtoBrief;
import com.zxcc.blog.article.service.ArticleService;
import com.zxcc.blog.base.dto.PageInfo;
import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 文章类别
 *
 * Created by xuanzh.cc on 2016/9/7.
 */
@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/{categoryId}/detail")
    public ModelAndView categoryDetail(@PathVariable("categoryId") Long categoryId){
        ModelAndView mdv = new ModelAndView();

        List<CategoryDto> categories = this.categoryService.getCategoryList();
        mdv.addObject("categories", categories);
        mdv.setViewName("front/category/category-detail");

        if(categoryId != null && categoryId > 0) {
            List<ArticleDtoBrief> articles = this.articleService.getArticleByCategoryId(categoryId);
            mdv.addObject("articles", articles);
        }

        return mdv;
    }
}
