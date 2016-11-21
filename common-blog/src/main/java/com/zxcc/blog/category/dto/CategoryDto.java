package com.zxcc.blog.category.dto;

import java.util.Date;

/**
 * Created by xuanzh.cc on 2016/9/10.
 */
public class CategoryDto {
    private long categoryId;

    /* 名称 */
    private String categoryName;

    /* 创建时间 */
    private Date createTime;

    /* 显示优先级 */
    private int priority;

    /* 用户ID */
    private long userId;

    /* 该类别下文章总数 */
    private int totalArticleNum;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getTotalArticleNum() {
        return totalArticleNum;
    }

    public void setTotalArticleNum(int totalArticleNum) {
        this.totalArticleNum = totalArticleNum;
    }
}
