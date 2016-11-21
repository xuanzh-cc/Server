package com.zxcc.blog.category.service.impl;

import com.zxcc.blog.article.dao.ArticleDao;
import com.zxcc.blog.article.service.ArticleService;
import com.zxcc.blog.category.constant.CategoryOperationMessage;
import com.zxcc.blog.category.dao.CategoryDao;
import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.entity.Category;
import com.zxcc.blog.category.exception.CategoryNameRepeatedException;
import com.zxcc.blog.category.exception.CategoryNotExistException;
import com.zxcc.blog.category.service.CategoryService;
import com.zxcc.blog.tag.dao.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/11.
 */
@Component
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagDao tagDao;

    @Override
    public List<CategoryDto> getCategoryList() {
        return categoryDao.queryAll(0, 100);
    }

    @Override
    @Transactional
    public int deleteById(long userId, long categoryId) {
        if(this.existCategory(userId, categoryId)) {
            //查询出该类别下的全部文章
            List<Long> articleIds = this.articleDao.queryArticleIdByCategoryId(categoryId);
            if(!articleIds.isEmpty()) {
                //删除全部的文章
                this.articleService.deleteByArticleIdList(articleIds);
            }
            //删除类别
            int result = this.categoryDao.deleteById(categoryId);
            return result;
        }
        return 0;
    }

    @Override
    public boolean existCategory(long userId, long categoryId) {
        int count = this.categoryDao.existId(userId, categoryId);
        return count > 0;
    }

    @Override
    public boolean existCategoryName(long userId, String categoryName) {
        int count = this.categoryDao.existName(userId, categoryName);
        return count > 0;
    }

    @Override
    @Transactional
    public int addCategory(Category category) throws CategoryNameRepeatedException {
        //类别名称已经存在
        if(this.existCategoryName(category.getUser().getUserId(), category.getCategoryName().toUpperCase())) {
            throw new CategoryNameRepeatedException(CategoryOperationMessage.CATEGORY_NAME_REPEATED);
        }

        int result = this.categoryDao.insertCategory(category);

        return result;
    }

    @Override
    @Transactional
    public int updateCategory(Category category) throws CategoryNameRepeatedException, CategoryNotExistException {
        long userId = category.getUser().getUserId();

        //用户该类别不存在
        CategoryDto userCategory = this.categoryDao.queryById(category.getCategoryId());
        if(userCategory == null || userCategory.getUserId() != userId){
            throw new CategoryNotExistException(CategoryOperationMessage.CATEGORY_NOT_EXIST);
        }

        //名称改变,判断是否重复返回
        if(!userCategory.getCategoryName().equalsIgnoreCase(category.getCategoryName())){
            if(this.existCategoryName(userId, category.getCategoryName().toUpperCase())) {
                throw new CategoryNameRepeatedException(CategoryOperationMessage.CATEGORY_NAME_REPEATED);
            }
        }

        int result = this.categoryDao.updateCategory(category);
        return result;
    }
}
