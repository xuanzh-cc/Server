package com.zxcc.resources.annotation;

import com.zxcc.resources.constant.ResourcesType;

import java.lang.annotation.*;

/**
 * 资源标识类类
 * Created by xuanzh.cc on 2016/6/27.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Resources {
    /**
     * 资源类型，默认为EXCEL
     * @return
     */
    ResourcesType type() default ResourcesType.EXCEL;

    /**
     * 资源文件名称
     * @return
     */
    String value() default "";

}
