package com.zxcc.blog.category.controller.admin;

import com.zxcc.blog.article.constant.ArticleOperationMessage;
import com.zxcc.blog.category.constant.CategoryOperationMessage;
import com.zxcc.blog.category.dto.CategoryDto;
import com.zxcc.blog.category.dto.CategoryOperationResult;
import com.zxcc.blog.category.entity.Category;
import com.zxcc.blog.category.exception.CategoryNameRepeatedException;
import com.zxcc.blog.category.exception.CategoryNotExistException;
import com.zxcc.blog.category.service.CategoryService;
import com.zxcc.blog.user.constant.UserConstant;
import com.zxcc.blog.user.constant.UserOperationMessage;
import com.zxcc.blog.web.constant.CommonMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/7.
 */
@Controller
@RequestMapping("/category/admin")
public class CategoryAdminController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 类别列表
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView categoryList(){
        ModelAndView mdv = new ModelAndView();

        List<CategoryDto> categories = this.categoryService.getCategoryList();
        mdv.addObject("categories", categories);

        mdv.setViewName("backend/category/category-list");
        return mdv;
    }

    /**
     * 新增类别
     * @param categoryName
     * @param priority
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public CategoryOperationResult newCategory(HttpSession session, String categoryName, Integer priority){
        CategoryOperationResult categoryOperationResult = new CategoryOperationResult();
        //用户登陆过期
        Long userId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
        if (userId == null) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(UserOperationMessage.USER_LOGIN_EXPIRED);
            return categoryOperationResult;
        }

        //类别名称为空
        if (StringUtils.isEmpty(categoryName)) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(CategoryOperationMessage.CATEGORY_NAME_EMPTY);
            return categoryOperationResult;
        }

        if(priority == null || priority <= 0){
            priority = Integer.MAX_VALUE / 2;
        }

        Category category = Category.valueOf(userId, categoryName, priority);

        //添加类别
        try{
            int result = this.categoryService.addCategory(category);
            categoryOperationResult.setSuccess(result > 0);
            return categoryOperationResult;
        } catch (CategoryNameRepeatedException e) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(e.getMessage());
            return  categoryOperationResult;
        }
    }

    /**
     * 更新类别
     * @param session
     * @param categoryId
     * @param categoryName
     * @param priority
     * @return
     */
    @RequestMapping(value = "/{categoryId}/update", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public CategoryOperationResult updateCategory(HttpSession session, @PathVariable("categoryId") Long categoryId, String categoryName, Integer priority){
        CategoryOperationResult categoryOperationResult = new CategoryOperationResult();
        //用户登陆过期
        Long userId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
        if (userId == null) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(UserOperationMessage.USER_LOGIN_EXPIRED);
            return categoryOperationResult;
        }

        //请求参数错误
        if (categoryId == null || categoryId <= 0) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(CommonMessage.REQ_PARAM_ERROR);
            return categoryOperationResult;
        }

        //类别名称为空
        if (StringUtils.isEmpty(categoryName)) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(CategoryOperationMessage.CATEGORY_NAME_EMPTY);
            return categoryOperationResult;
        }

        if(priority == null || priority <= 0){
            priority = Integer.MAX_VALUE / 2;
        }

        Category category = Category.valueOf(userId, categoryName, priority);
        category.setCategoryId(categoryId);

        //更新类别
        try{
            int result = this.categoryService.updateCategory(category);
            categoryOperationResult.setSuccess(result > 0);
            return categoryOperationResult;
        } catch (CategoryNameRepeatedException e) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(e.getMessage());
            return  categoryOperationResult;
        } catch (CategoryNotExistException e) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(e.getMessage());
            return  categoryOperationResult;
        }
    }

    /**
     * 根据 ID 删除类别
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("/{categoryId}/delete")
    @ResponseBody
    public CategoryOperationResult deleteCategory(HttpSession session, @PathVariable("categoryId")Long categoryId){
        CategoryOperationResult categoryOperationResult = new CategoryOperationResult();

        //用户登陆过期
        Long userId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
        if (userId == null) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(UserOperationMessage.USER_LOGIN_EXPIRED);
            return categoryOperationResult;
        }

        //请求参数错误
        if (categoryId == null || categoryId <= 0) {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(CommonMessage.REQ_PARAM_ERROR);
            return categoryOperationResult;
        }

        int result = this.categoryService.deleteById(userId, categoryId);
        if(result > 0){
            categoryOperationResult.setSuccess(true);
        } else {
            categoryOperationResult.setSuccess(false);
            categoryOperationResult.setMsg(CategoryOperationMessage.CATEGORY_NOT_EXIST);
        }

        return categoryOperationResult;
    }
}
