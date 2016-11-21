package com.zxcc.resources.annotation;

import com.zxcc.resources.constant.InjectType;

import java.lang.annotation.*;

/**
 * 用于标识 使用 spring 容器中的bean来注入到资源类的实例
 * Created by xuanzh.cc on 2016/6/28.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {

    String value() default "";
}
