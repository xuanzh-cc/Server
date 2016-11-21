package com.zxcc.blog.search.controller;

import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.service.CategoryService;
import com.zxcc.blog.search.service.SearchService;
import com.zxcc.utility.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/13.
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 检索
     * @param keyword
     * @return
     */
    @RequestMapping("/s")
    private ModelAndView search(String keyword){
        ModelAndView mdv = new ModelAndView();
        if(!StringUtils.isEmpty(keyword)) {
            keyword = StringUtils.URLDecode(keyword);
            List<ArticleDtoDetail> articles =  this.searchService.search(keyword);
            mdv.addObject("articles", articles);
        }
        List<CategoryDto> categories = this.categoryService.getCategoryList();
        mdv.addObject("categories", categories);
        mdv.setViewName("front/search/search-result");
        return mdv;
    }
}
