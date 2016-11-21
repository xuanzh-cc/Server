package com.zxcc.blog.article.dao;

import com.zxcc.blog.article.dto.ArticleDtoBrief;
import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.article.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuanzh.cc on 2016/8/14.
 */
public interface ArticleDao {

    /**
     * 添加文章
     * @param article
     * @return
     */
    int insertArticle(@Param("article") Article article);

    /**
     * 查询范围内的全部文章简短信息
     * @param offset
     * @param limit
     * @return
     */
    List<ArticleDtoBrief> queryAllBrief(@Param("offset")int offset, @Param("limit")int limit);

    /**
     * 根据ID查询文章的详细信息
     * @param articleId
     * @return
     */
    ArticleDtoDetail queryDetailById(@Param("articleId")long articleId);

    /**
     * 查找 指定ID 的文章数量， 用来判断文章是否存在
     *
     * @param userId
     * @param articleId
     * @return
     */
    int existId(@Param("userId")long userId, @Param("articleId") long articleId);

    /**
     * 更新文章
     * @param article
     * @return
     */
    int updateArticle(@Param("article")Article article);

    /**
     * 根据 ID 删除文章
     * @param articleId
     * @return
     */
    int deleteById(long articleId);

    /**
     * 查询指定类别下的全部文章ID
     * @param categoryId
     * @return
     */
    List<Long> queryArticleIdByCategoryId(long categoryId);

    /**
     * 根据文章ID列表 批量删除文章
     * @param articleIdList
     * @return
     */
    int deleteByArticleIdList(@Param("articleIdList") List<Long> articleIdList);

    /**
     * 查询全部文章的数目
     * @return
     */
    int getTotalNum();

    /**
     * 根据ID查询文章
     * @param categoryId
     * @return
     */
    List<ArticleDtoBrief> queryByCategoryId(@Param("categoryId")long categoryId);

    /**
     * 增加点击次数
     * @param articleId
     */
    void incrClickHit(@Param("articleId")long articleId);
}
