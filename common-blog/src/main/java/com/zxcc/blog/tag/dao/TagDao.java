package com.zxcc.blog.tag.dao;

import org.apache.ibatis.annotations.Param;
import com.zxcc.blog.tag.entity.Tag;

import java.util.List;
import java.util.Set;

/**
 * Created by xuanzh.cc on 2016/9/10.
 */
public interface TagDao {

    /**
     * 插入文章对应的标签
     * @param articleId
     * @param tags
     * @return
     */
    int insertArticleTags(@Param("articleId")long articleId, @Param("tags")Set<Tag> tags);

    /**
     * 删除 article 对应的全部 tag
     * @param articleId
     */
    void deleteAllTagIdByArticleId(@Param("articleId")long articleId);

    /**
     * 根据文章id列表 删除对应的全部tag
     * @param articleIdList
     */
    void deleteAllTagIdByArticleIdList(@Param("articleIdList") List<Long> articleIdList);
}
