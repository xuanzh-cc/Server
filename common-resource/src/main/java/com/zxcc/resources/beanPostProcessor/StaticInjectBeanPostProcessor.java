package com.zxcc.resources.beanPostProcessor;

import com.zxcc.resources.Storage;
import com.zxcc.resources.StorageManager;
import com.zxcc.resources.annotation.Id;
import com.zxcc.resources.annotation.Static;
import com.zxcc.resources.constant.StaticInjectType;
import com.zxcc.utility.ReflectionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 静态资源注入处理器，负责完成 {@link Static } 声明的资源注入工作
 * Created by xuanzh.cc on 2016/7/7.
 */
public class StaticInjectBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter{
    private Logger logger = LoggerFactory.getLogger(StaticInjectBeanPostProcessor.class);

    @Autowired
    private StorageManager storageManager;
    @Autowired
    private ConversionService conversionService;

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, String beanName) throws BeansException {

        ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                Static anno = field.getAnnotation(Static.class);
                if(anno == null) {
                    return ;
                }

                StaticInjectType staticInjectType = checkStaticInjectType(field);

                switch (staticInjectType) {
                    case STORAGE:
                        staticInjectStorage(bean, field, anno);
                        break;
                    case INSTANCE:
                        staticInjectInstance(bean, field, anno);
                        break;
                }


            }

        }, new ReflectionUtils.FieldFilter() {
            @Override
            public boolean matches(Field field) {
                return field.isAnnotationPresent(Static.class);
            }
        });

        return super.postProcessAfterInstantiation(bean, beanName);
    }

    /**
     * 注入静态资源实例
     * @param bean
     * @param field
     * @param anno
     */
    private void staticInjectInstance(Object bean, Field field, Static anno) {
        //获取注入资源的唯一标识class
        Class clazz = this.getIdType(field.getType());
        //唯一标识的值
        Object key = this.conversionService.convert(anno.value(), clazz);

        Storage storage = this.storageManager.getStorage(field.getType());

        //TODO 添加监听器

        Object instance = storage.get(key, false);
        if(anno.required() && instance == null) {
            FormattingTuple message = MessageFormatter.format("属性[{}]的注入值不存在", field);
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage());
        }
        inject(bean, field, instance);
    }

    /**
     * 获取唯一标识类型
     * @param type
     * @return
     */
    private Class getIdType(Class<?> type) {
        Field field = ReflectionUtility.getFirstDeclaredFieldWithAnnotationPresent(type, Id.class);
        return field.getType();
    }

    /**
     * 注入存储空间对象
     * @param bean
     * @param field
     * @param anno
     */
    private void staticInjectStorage(Object bean, Field field, Static anno) {
        Type type = field.getGenericType();
        //如果不是参数化类型
        if (!(type instanceof ParameterizedType)) {
            String message = "类型声明不正确";
            logger.error(message);
            throw new RuntimeException(message);
        }

        Type[] types = ((ParameterizedType) type).getActualTypeArguments();

        if (!(types[1] instanceof Class)) {
            String message = "类型声明不正确";
            logger.error(message);
            throw new RuntimeException(message);
        }

        //资源类型
        Class clazz = (Class) types[1];
        Storage storage = this.storageManager.getStorage(clazz);

        boolean required = anno.required();
        if (required && storage == null) {
            FormattingTuple message = MessageFormatter.format("静态资源类[{}]不存在", clazz);
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage());
        }

        this.inject(bean, field, storage);
    }

    /**
     * 注入属性值
     * @param bean
     * @param field
     * @param value
     */
    private void inject(Object bean, Field field, Object value) {
        try {
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            FormattingTuple message = MessageFormatter.format("属性[{}]注入失败", field);
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage(), e);
        }
    }

    /**
     * 检测注入类型
     * @param field
     * @return
     */
    private StaticInjectType checkStaticInjectType(Field field) {
        if (field.getType().equals(Storage.class)) {
            return StaticInjectType.STORAGE;
        }
        return StaticInjectType.INSTANCE;
    }
}

