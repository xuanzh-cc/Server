package com.zxcc.blog.category.service;

import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.entity.Category;
import com.zxcc.blog.category.exception.CategoryNameRepeatedException;
import com.zxcc.blog.category.exception.CategoryNotExistException;

import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/11.
 */
public interface CategoryService {

    /**
     * 获取类别列表
     * @return
     */
    public List<CategoryDto> getCategoryList();

    /**
     * 根据 ID 删除类别
     *
     * @param userId
     * @param categoryId
     * @return
     */
    int deleteById(long userId, long categoryId);

    /**
     * 判断 类别是否存在
     * @param userId
     * @param categoryId
     * @return
     */
    boolean existCategory(long userId, long categoryId);

    /**
     * 判断是否存在相同的类别名称
     * @param userId
     * @param categoryName
     * @return
     */
    boolean existCategoryName(long userId, String categoryName);

    /**
     * 添加类别
     * @param category
     */
    int addCategory(Category category) throws CategoryNameRepeatedException;

    /**
     * 更新类别
     * @param category
     * @return
     * @throws CategoryNameRepeatedException
     * @throws CategoryNotExistException
     */
    int updateCategory(Category category)  throws CategoryNameRepeatedException, CategoryNotExistException;
}
