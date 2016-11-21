package com.zxcc.blog.base.service;

import com.zxcc.blog.base.dto.PageInfo;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

/**
 * Created by xuanzh.cc on 2016/9/15.
 */
public class BaseService {

    /** 页面上最多显示的分页数目 */
    @Value("${web.showPageNum}")
    private int showPageNum;

    @PostConstruct
    public void init(){
        PageInfo.setShowPageNum(this.showPageNum);
    }
}
