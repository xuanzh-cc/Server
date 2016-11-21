package com.zxcc.blog.user.service.impl;

import com.zxcc.blog.user.entity.User;
import com.zxcc.blog.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by xuanzh.cc on 2016/8/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    public void getByUserNameAndPassword() throws Exception {
        User user = userService.getByUsername("zx");
        System.out.println(user);
    }

    @Test
    public void passwordModify() throws Exception {
        userService.passwordModify(1L, "123455", "123");
    }

}