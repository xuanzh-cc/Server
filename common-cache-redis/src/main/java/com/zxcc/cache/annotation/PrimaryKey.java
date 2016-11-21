package com.zxcc.cache.annotation;

import java.lang.annotation.*;

/**
 * 实体主键标识
 * Created by xuanzh.cc on 2016/7/23.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PrimaryKey {
}
