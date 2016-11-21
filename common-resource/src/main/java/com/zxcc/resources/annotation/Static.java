package com.zxcc.resources.annotation;

import java.beans.PropertyEditor;
import java.lang.annotation.*;

/**
 * 资源注入注解
 * Created by xuanzh.cc on 2016/7/7.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Static {
    /**
     * 表示在
     * @return
     */
    String value() default "";

    /**
     * 唯一标识转换声明
     * @return
     */
    Class<? extends PropertyEditor> converter() default PropertyEditor.class;

    /**
     * 注入值是否是必须
     * @return
     */
    boolean required();

}
