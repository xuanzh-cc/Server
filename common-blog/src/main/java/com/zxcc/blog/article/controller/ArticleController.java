package com.zxcc.blog.article.controller;

/**
 * 文章模块 controller
 * Created by xuanzh.cc on 2016/8/21.
 */

import com.zxcc.blog.article.dto.ArticleDtoBrief;
import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.article.dto.ArticleOperationResult;
import com.zxcc.blog.article.dto.EditContentInfo;
import com.zxcc.blog.article.service.ArticleService;
import com.zxcc.blog.base.dto.PageInfo;
import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/{articleId}/detail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView articleDetail(@PathVariable("articleId")Long articleId){
        ModelAndView mdv = new ModelAndView();

        List<CategoryDto> categories = this.categoryService.getCategoryList();

        mdv.addObject("categories", categories);

        mdv.setViewName("front/article/article-detail");

        if(articleId != null && articleId > 0) {
            ArticleDtoDetail article = this.articleService.viewArticle(articleId);
            mdv.addObject("article", article);
        }
        return mdv;
    }

    /**
     * 获取当前页面的 文章数
     * @param pageNum 页面num
     * @return
     */
    @RequestMapping(value = "/page/{pageNum}")
    public ModelAndView page(@PathVariable("pageNum")Integer pageNum, Integer pageSize){
        ModelAndView mdv = new ModelAndView();

        pageNum = (pageNum == null || pageNum < 1) ? 1 : pageNum;
        pageSize = (pageSize == null || pageSize < 1) ? 1 : pageSize;

        PageInfo pageInfo = this.articleService.getPageInfo(pageNum, pageSize);

        List<ArticleDtoBrief> articles = this.articleService.getArticleBriefList(pageInfo.getOffset(), pageInfo.getLimit());

        List<CategoryDto> categories = this.categoryService.getCategoryList();

        mdv.addObject("pageInfo", pageInfo);
        mdv.addObject("articles", articles);
        mdv.addObject("categories", categories);

        mdv.setViewName("front/index" );

        return mdv;
    }
}
