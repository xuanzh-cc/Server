package com.zxcc.blog.web.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * http session 工具
 * Created by xuanzh.cc on 2016/9/10.
 */
public class HttpSessionUtils {

    /**
     * 获取当前的httpSession 对象
     * @return
     */
    public static HttpSession getSession(){
        HttpSession session = getRequest().getSession(true);
        return session;
    }

    /**
     * 获取 httpRequest
     * @return
     */
    private static HttpServletRequest getRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
