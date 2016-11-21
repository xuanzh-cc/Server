package com.zxcc.blog.article.entity;

import com.zxcc.blog.category.entity.Category;
import com.zxcc.blog.tag.entity.Tag;
import com.zxcc.blog.user.entity.User;
import com.zxcc.utility.DateUtils;
import com.zxcc.utility.IdFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 文章
 * Created by xuanzh.cc on 2016/8/14.
 */
public class Article implements Serializable {

    private static final long serialVersionUID = -3952560380659893446L;

    /** 文章id */
    private long articleId;
    /** 文章标题 */
    private String title;
    /** 文章内容 */
    private String content;
    /** 文章摘要 */
    private String summary;
    /** 点击次数 */
    private int clickHit;
    /** 文章创建时间 */
    private Date createTime;
    /** 文章作者 */
    private User user;
    /** 文章类别 */
    private Category category;
    /** 标签 */
    private Set<Tag> tags = new HashSet<Tag>(0);

    /**
     * 文章实例化方法
     * @return
     * @param userId
     * @param title
     * @param categoryId
     * @param contentHtml
     * @param contentPlain
     * @param tagIds
     */
    public static Article valueOf(long userId, String title, long categoryId, String contentHtml, String contentPlain, HashSet<Long> tagIds){
        Article article = new Article();
        article.articleId = IdFactory.nextId64Bit();
        article.title = title;
        article.content = contentHtml;
        article.summary = contentPlain.length() < 200 ? contentPlain : contentPlain.substring(0, 200);
        article.summary = article.summary.replaceAll("<", "");
        article.summary = article.summary.replaceAll(">", "");
        article.clickHit = 0;
        article.createTime = DateUtils.now();
        article.user = User.valueOf(userId);
        article.category = Category.valueOf(categoryId);
        for(Long tagId : tagIds) {
            article.tags.add(Tag.valueOf(tagId));
        }
        return article;
    }

    /**
     * 文章实例化方法
     * @return
     * @param userId
     * @param title
     * @param categoryId
     * @param contentHtml
     * @param contentPlain
     * @param tagIds
     */
    public static Article valueOf(long userId, long articleId, String title, long categoryId, String contentHtml, String contentPlain, HashSet<Long> tagIds){
        Article article = valueOf(userId, title, categoryId, contentHtml, contentPlain, tagIds);
        article.articleId = articleId;
        return article;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
