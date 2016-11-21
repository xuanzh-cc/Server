package com.zxcc.blog.article.service;

import com.zxcc.blog.article.dto.ArticleDtoBrief;
import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.article.entity.Article;
import com.zxcc.blog.base.dto.PageInfo;

import java.util.List;

/**
 * Created by xuanzh.cc on 2016/8/14.
 */
public interface ArticleService {
    /**
     * 获取文章数目
     * @return
     */
    int getTotalNum();

    /**
     * 获得总页数
     * @param pageSize 每页显示的数量
     * @return
     */
    int getTotalPageNum(int pageSize);

    /**
     * 增加文章
     * @param article
     * @return
     */
    int addArticle(Article article);

    /**
     * 获取全部的文章
     * @return
     * @param offset
     * @param limit
     */
    List<ArticleDtoBrief> getArticleBriefList(int offset, int limit);

    /**
     * 根据文章ID 获取文章详细信息
     * @param articleId
     * @return
     */
    ArticleDtoDetail getArticleDetailById(long articleId);

    /**
     * 前台根据ID查询文章，并将浏览次数 +1
     * @param articleId
     * @return
     */
    ArticleDtoDetail viewArticle(long articleId);

    /**
     * 判断文章是否存在
     * @param userId
     * @param articleId
     * @return
     */
    boolean existArticle(long userId, long articleId);

    /**
     * 更新 article
     * @param article
     * @return
     */
    int updateArticle(Article article);

    /**
     * 删除文章
     *
     * @param userId
     * @param articleId
     * @return
     */
    int deleteById(long userId, long articleId);

    /**
     * 根据文章ID列表， 批量删除文章
     * @param articleIds
     * @return
     */
    int deleteByArticleIdList(List<Long> articleIds);

    /**
     * 获取分页信息
     * @param currentPageNum
     * @param pageSize
     * @return
     */
    PageInfo getPageInfo(int currentPageNum, int pageSize);

    /**
     * 根据类别查询文章
     * @param categoryId
     * @return
     */
    List<ArticleDtoBrief> getArticleByCategoryId(long categoryId);
}
