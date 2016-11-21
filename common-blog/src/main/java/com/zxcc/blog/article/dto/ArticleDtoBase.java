package com.zxcc.blog.article.dto;

import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.entity.Category;
import com.zxcc.blog.tag.dto.TagDto;
import com.zxcc.blog.tag.entity.Tag;
import com.zxcc.blog.user.dto.UserDto;
import com.zxcc.blog.user.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 文章dto 父类
 * Created by xuanzh.cc on 2016/8/21.
 */
public abstract class ArticleDtoBase {
    //文章ID
    protected long articleId;
    //标题
    protected String title;
    //点击次数
    private int clickHit;
    //创建时间
    protected Date createTime;
    //文章类别
    protected CategoryDto category;
    //文章作者
    private UserDto user;
    //标签
    protected Set<TagDto> tags;

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClickHit() {
        return clickHit;
    }

    public void setClickHit(int clickHit) {
        this.clickHit = clickHit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }
}
