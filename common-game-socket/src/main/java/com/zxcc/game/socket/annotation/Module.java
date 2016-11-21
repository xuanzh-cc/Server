package com.zxcc.game.socket.annotation;

import java.lang.annotation.*;

/**
 * 模块标识
 * Created by xuanzh.cc on 2016/10/10.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Module {
    /** 模块号 */
    int value();
}
