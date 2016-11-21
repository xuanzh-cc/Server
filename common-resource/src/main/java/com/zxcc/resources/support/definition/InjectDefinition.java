package com.zxcc.resources.support.definition;

import com.zxcc.resources.annotation.Inject;
import com.zxcc.resources.constant.InjectType;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;

/**
 * 属性注入信息定义
 * Created by xuanzh.cc on 2016/6/28.
 */
public class InjectDefinition {
    //被注入的属性
    private final Field field;
    //注入类型
    private InjectType injectType;
    //属性注入配置
    private Inject inject;

    public InjectDefinition(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("被注入的属性不能为空");
        }
        if(!field.isAnnotationPresent(Inject.class)) {
            throw new IllegalArgumentException("被注入的属性域" + field.getName() + "的注解配置不存在！");
        }
        Inject inject = field.getAnnotation(Inject.class);
        this.inject = inject;
        this.field = field;
        String beanName = this.inject.value();
        //配置的值为空或者空串，则按类型注入，否则按名称注入
        if(StringUtils.isBlank(beanName)){
            this.injectType = InjectType.BY_TYPE;
        } else {
            this.injectType = InjectType.BY_NAME;
        }

    }

    public Field getField() {
        return field;
    }

    public InjectType getInjectType() {
        return injectType;
    }

    public void setInjectType(InjectType injectType) {
        this.injectType = injectType;
    }

    /**
     * 获取要注入的值
     * @param applicationContext
     * @return
     */
    public Object getValue(ApplicationContext applicationContext) {
        switch (injectType) {
            case BY_NAME:
                return applicationContext.getBean(this.inject.value());
            case BY_TYPE:
                return applicationContext.getBean(field.getType());
            default:
                return null;
        }
    }
}
