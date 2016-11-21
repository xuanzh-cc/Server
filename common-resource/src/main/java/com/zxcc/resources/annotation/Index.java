package com.zxcc.resources.annotation;

import java.lang.annotation.*;
import java.util.Comparator;

/**
 * 资源索引
 * Created by xuanzh.cc on 2016/6/27.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Index {
    /**
     * 索引名称
     * @return
     */
    String name();

    /**
     * 索引值是否唯一
     * @return
     */
    boolean unique() default false;

    /**
     * 排序比较器
     * @return
     */
    Class<? extends Comparator> comparator() default Comparator.class;

}
