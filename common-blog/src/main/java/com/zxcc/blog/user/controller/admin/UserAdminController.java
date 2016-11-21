package com.zxcc.blog.user.controller.admin;

import com.zxcc.blog.user.constant.UserConstant;
import com.zxcc.blog.user.constant.UserOperationMessage;
import com.zxcc.blog.user.dto.UserOperationResult;
import com.zxcc.blog.user.entity.User;
import com.zxcc.blog.user.exception.UserNotExistException;
import com.zxcc.blog.user.exception.UserNotLoginException;
import com.zxcc.blog.user.exception.UserPasswordNotMatchException;
import com.zxcc.blog.user.service.UserService;
import com.zxcc.blog.web.constant.CommonMessage;
import com.zxcc.utility.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块 controller
 * Created by xuanzh.cc on 2016/8/4.
 */
@Controller
@RequestMapping("/user/admin")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @RequestMapping("/loginPage")
    public String loginPage(){
        return "backend/user/login/login";
    }

    /**
     * 登陆
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/login", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public UserOperationResult login(HttpSession session, String username, String password){
        User user = this.userService.validLogin(username, password);
        //验证成功
        if (user != null) {
            session.setAttribute(UserConstant.USER_SESSION_KEY, user.getUserId());
            return new UserOperationResult(true);
        }
        return new UserOperationResult(false, UserOperationMessage.LOGIN_ERROR);
    }

    /**
     * 登出
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/admin/loginPage";
    }

    /**
     * 修改密码
     * @param password
     * @return
     */
    @RequestMapping(value = "/passwordModify", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public UserOperationResult<Void> passwordModify(HttpSession session, String password, String oldPassword){
        try {
            if(StringUtils.isBlank(password) || StringUtils.isBlank(oldPassword) ){
                return new UserOperationResult(false, CommonMessage.REQ_PARAM_ERROR);
            }

            Long loginUserId = (Long) session.getAttribute(UserConstant.USER_SESSION_KEY);
            if(loginUserId == null) {
                return new UserOperationResult(false, UserOperationMessage.USER_NOT_LOGIN);
            }

            this.userService.passwordModify(loginUserId, password, oldPassword);
            return new UserOperationResult(true);
        } catch (UserPasswordNotMatchException e) {
            return new UserOperationResult(false, e.getMessage());
        }
    }

}
