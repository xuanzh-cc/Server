package com.zxcc.blog.user.dao;

import com.zxcc.blog.user.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by xuanzh.cc on 2016/8/4.
 */
public interface UserDao {

    /**
     * 添加用户
     * @param user
     * @return
     */
    int insertUser(@Param("user") User user);

    /**
     * 根据 ID 查询
     * @param userId
     * @return
     */
    User queryById(@Param("userId") long userId);

    /**
     * 根据 用户名和密码 查询
     * @param username
     * @return
     */
    User queryByUsername(@Param("username") String username);

    /**
     * 更新用户密码
     * @param userId
     * @param password
     * @return
     */
    int updateUserPassword(@Param("userId")long userId, @Param("password")String password);
}
