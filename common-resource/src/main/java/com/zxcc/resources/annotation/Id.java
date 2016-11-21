package com.zxcc.resources.annotation;

import java.lang.annotation.*;

/**
 * 资源唯一标识
 * Created by xuanzh.cc on 2016/6/27.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Id {
    String value() default "";
}
