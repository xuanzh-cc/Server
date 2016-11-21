package com.zxcc.blog.article.controller.admin;

/**
 * 文章模块 controller
 * Created by xuanzh.cc on 2016/8/21.
 */

import com.zxcc.blog.article.constant.ArticleOperationMessage;
import com.zxcc.blog.article.dto.ArticleDtoBrief;
import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.article.dto.ArticleOperationResult;
import com.zxcc.blog.article.dto.EditContentInfo;
import com.zxcc.blog.article.entity.Article;
import com.zxcc.blog.article.exception.ArticleNotExistException;
import com.zxcc.blog.article.service.ArticleService;
import com.zxcc.blog.base.dto.PageInfo;
import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.exception.CategoryNotExistException;
import com.zxcc.blog.category.service.CategoryService;
import com.zxcc.blog.user.constant.UserConstant;
import com.zxcc.blog.user.constant.UserOperationMessage;
import com.zxcc.blog.web.constant.CommonMessage;
import com.zxcc.utility.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/article/admin")
public class ArticleAdminController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 发布编辑页面
     * @param articleId 文章id， 如果不为空，则为编辑，否则为新建
     * @return
     */
    @RequestMapping(value = "/editorPage", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getEditorPage(Long articleId){
        ModelAndView mdv = new ModelAndView();
        //新建文章
        if(articleId == null || articleId <= 0) {
            EditContentInfo editContentInfo = new EditContentInfo(true);
            mdv.addObject("editContentInfo", editContentInfo);
        } else {
            ArticleDtoDetail article = this.articleService.getArticleDetailById(articleId);
            if(article == null) {
                mdv.setViewName("redirect:/article/admin/list");
                return mdv;
            }
            EditContentInfo editContentInfo = new EditContentInfo(false, article);
            mdv.addObject("editContentInfo", editContentInfo);
        }

        //类别信息
        List<CategoryDto> categories = this.categoryService.getCategoryList();

        mdv.addObject("categories", categories);

        mdv.setViewName("backend/article/article-editor");
        return mdv;
    }

    /**
     * 发布文章
     * @param title
     * @param contentHtml
     * @param contentPlain
     * @param categoryId
     * @param tags
     * @return
     */
    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ArticleOperationResult newArticle(HttpSession session, String title, long categoryId, String contentHtml, String contentPlain, @RequestParam("tags[]")HashSet<Long> tags){
        ArticleOperationResult articleOperationResult = new ArticleOperationResult();
        Long userId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
        if (userId == null) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(UserOperationMessage.USER_LOGIN_EXPIRED);
            return articleOperationResult;
        }

        Article article = Article.valueOf(userId, title, categoryId, contentHtml, contentPlain, tags);

        try{
            int result = articleService.addArticle(article);
            articleOperationResult.setSuccess(result > 0);
            return articleOperationResult;
        }catch (CategoryNotExistException e) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(e.getMessage());
            return articleOperationResult;
        }
    }

    /**
     * 文章列表
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView articleList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "5")Integer pageSize){
        ModelAndView mdv = new ModelAndView();

        PageInfo pageInfo = this.articleService.getPageInfo(page, pageSize);
        mdv.addObject("pageInfo", pageInfo);

        List<ArticleDtoBrief> articles = this.articleService.getArticleBriefList(pageInfo.getOffset(), pageInfo.getLimit());
        mdv.addObject("articles", articles);
        mdv.setViewName("backend/article/article-list");
        return mdv;
    }

    /**
     * 文章详情
     * @return
     */
    @RequestMapping(value = "/{articleId}/detail", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView articleDetail(@PathVariable("articleId")Long articleId){
        ModelAndView mdv = new ModelAndView();
        ArticleDtoDetail article = this.articleService.getArticleDetailById(articleId);
        mdv.addObject("article", article);
        mdv.setViewName("backend/article/article-detail");
        return mdv;
    }

    /**
     * 文章更新
     * @param session
     * @param articleId
     * @param title
     * @param categoryId
     * @param contentHtml
     * @param contentPlain
     * @param tags
     * @return
     */
    @RequestMapping(value = "/{articleId}/update", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ArticleOperationResult updateArticle(HttpSession session, @PathVariable("articleId")Long articleId, String title, long categoryId, String contentHtml, String contentPlain, @RequestParam("tags[]")HashSet<Long> tags){
        ArticleOperationResult articleOperationResult = new ArticleOperationResult();

        //用户登陆过期
        Long userId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
        if (userId == null) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(UserOperationMessage.USER_LOGIN_EXPIRED);
            return articleOperationResult;
        }

        //请求参数错误
        if (articleId == null || articleId <= 0) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(CommonMessage.REQ_PARAM_ERROR);
            return articleOperationResult;
        }

        Article article = Article.valueOf(userId, articleId, title, categoryId, contentHtml, contentPlain, tags);

        try{
            int result = articleService.updateArticle(article);
            articleOperationResult.setSuccess(result > 0);
            return articleOperationResult;
        }catch (ArticleNotExistException e) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(e.getMessage());
            return articleOperationResult;
        }
        catch (CategoryNotExistException e) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(e.getMessage());
            return articleOperationResult;
        }
    }

    /**
     * 删除文章
     * @param session
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/{articleId}/delete", method = RequestMethod.POST)
    @ResponseBody
    public ArticleOperationResult deleteArticle(HttpSession session, @PathVariable("articleId")Long articleId){
        ArticleOperationResult articleOperationResult = new ArticleOperationResult();

        //用户登陆过期
        Long userId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
        if (userId == null) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(UserOperationMessage.USER_LOGIN_EXPIRED);
            return articleOperationResult;
        }

        //请求参数错误
        if (articleId == null || articleId <= 0) {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(CommonMessage.REQ_PARAM_ERROR);
            return articleOperationResult;
        }

        int result = this.articleService.deleteById(userId, articleId);
        if(result > 0){
            articleOperationResult.setSuccess(true);
        } else {
            articleOperationResult.setSuccess(false);
            articleOperationResult.setMsg(ArticleOperationMessage.ARTICLE_NOT_EXIST);
        }

        return articleOperationResult;
    }

    /**
     * 心跳，在编辑文章的时候防止session过期
     * @param session
     * @return
     */
    @RequestMapping(value = "heartbeat", method = RequestMethod.GET)
    @ResponseBody
    public Map heartbeat(HttpSession session){
        Map result = new HashMap();
        result.put("success", true);
        return result;
    }
}
