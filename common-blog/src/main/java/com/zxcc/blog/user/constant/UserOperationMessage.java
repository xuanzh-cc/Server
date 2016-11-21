package com.zxcc.blog.user.constant;

/**
 * Created by xuanzh.cc on 2016/8/4.
 */
public interface UserOperationMessage {

    String LOGIN_ERROR = "用户名或者密码错误！" ;

    String PASSWORD_MODIFY_ERROR = "密码修改失败！";

    String USER_NOT_EXIST = "该用户不存在！";

    String USER_LOGIN_EXPIRED = "用户登录失效！";

    String USER_NOT_LOGIN = "用户未登陆！";

    String PASSWORD_NOT_MATCH = "密码不匹配！";

}
