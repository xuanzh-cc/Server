package com.zxcc.cache.annotation;

import java.lang.annotation.*;

/**
 * 实体索引
 * Created by xuanzh.cc on 2016/7/23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Index {
    String name() default "";
}
