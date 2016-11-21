package com.zxcc.blog.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户模块 controller
 * Created by xuanzh.cc on 2016/8/4.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    /**
     * 关于我
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}/about", method = {RequestMethod.POST, RequestMethod.GET})
    public String  about(@PathVariable("userId") Integer userId){
        return "";
    }

}
