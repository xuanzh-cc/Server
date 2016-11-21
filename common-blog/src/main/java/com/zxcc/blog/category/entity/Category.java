package com.zxcc.blog.category.entity;

import com.zxcc.blog.user.entity.User;
import com.zxcc.utility.DateUtils;
import com.zxcc.utility.IdFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章类别
 * Created by xuanzh.cc on 2016/8/14.
 */
public class Category implements Serializable{

    private static final long serialVersionUID = 8033417138244929056L;

    /** 类别ID */
    private long categoryId;
    /** 类别名称 */
    private String categoryName;
    /** 父类别 */
    private Category parent;
    /** 创建日期 */
    private Date createTime;
    /** 显示优先级 */
    private int priority;
    /** 所属用户ID */
    private User user;

    /**
     * 实例化方法
     * @param categoryId  类别ID
     * @return
     */
    public static Category valueOf(long categoryId) {
        Category category = new Category();
        category.categoryId = categoryId;
        return  category;
    }

    /**
     * 实例化方法
     * @param categoryName 类别名称
     * @param priority 优先级
     * @return
     */
    public static Category valueOf(long userId, String categoryName, int priority) {
        Category category = Category.valueOf(IdFactory.nextId64Bit());
        category.categoryName = categoryName;
        category.createTime = DateUtils.now();
        category.priority = priority;
        category.user = User.valueOf(userId);
        return category;
    }

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

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
