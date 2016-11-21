package com.zxcc.blog.tag.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuanzh.cc on 2016/9/7.
 */
@Controller
@RequestMapping("/tag/admin")
public class TagAdminController {

    /**
     * 标签列表
     * @return
     */
    @RequestMapping("/list")
    public String tagList(){

        return "backend/tag/tag-list";
    }
}
