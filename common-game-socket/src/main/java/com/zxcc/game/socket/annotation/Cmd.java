package com.zxcc.game.socket.annotation;

import java.lang.annotation.*;

/**
 * 命令号标识
 * Created by xuanzh.cc on 2016/10/10.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cmd {
    /** 指令号 */
    int value();
    /** 描述 */
    String desc();
}
