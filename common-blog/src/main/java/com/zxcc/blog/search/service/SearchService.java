package com.zxcc.blog.search.service;

import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.article.entity.Article;

import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/13.
 */
public interface SearchService {

    /**
     * 添加索引
     * @param article
     */
    void addIndex(Article article);

    /**
     * 更新索引
     * @param article
     */
    void updateIndex(Article article);

    /**
     * 删除索引
     * @param articleId
     */
    void deleteIndex(long articleId);

    /**
     * 根据关键字搜索
     * @param keywords
     * @return
     */
    List<ArticleDtoDetail> search(String keywords);

}
