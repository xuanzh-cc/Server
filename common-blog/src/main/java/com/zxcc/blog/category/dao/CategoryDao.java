package com.zxcc.blog.category.dao;

import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuanzh.cc on 2016/8/14.
 */
public interface CategoryDao {
    /**
     * 查询指定范围内的所有类别
     * @param offset
     * @param limit
     * @return
     */
    List<CategoryDto> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查找 指定ID 的类别数量， 用来判断类别是否存在
     * @param userId
     * @param categoryId
     * @return
     */
    int existId(@Param("userId")long userId, @Param("categoryId")long categoryId);

    /**
     * 根据类别ID删除类别
     * @param categoryId
     * @return
     */
    int deleteById(@Param("categoryId")long categoryId);

    /**
     * 查找指定名称 的类别数量，用来判断是否存在
     * @param userId
     * @param categoryName
     * @return
     */
    int existName(@Param("userId") long userId, @Param("categoryName")String categoryName);

    /**
     * 插入类别
     * @param category
     */
    int insertCategory(@Param("category") Category category);

    /**
     * 更新类别
     * @param category
     * @return
     */
    int updateCategory(@Param("category")Category category);

    /**
     * 根据ID查询类别
     * @param categoryId
     * @return
     */
    CategoryDto queryById(@Param("categoryId") long categoryId);
}
