package com.zxcc.utility;

import org.omg.CORBA.Object;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 * @see {@link ReflectionUtils;}
 * Created by xuanzh.cc on 2016/6/28.
 */
public abstract class ReflectionUtility extends ReflectionUtils{

    /**
     * 在给定的 clazz 的所有非static和非final字段上调用 fieldCallback， 不会调向上处理父类的字段。
     * @param clazz
     * @param fc  回调处理
     * @param ff    过滤处理
     */
    public static void doWithDeclaredFields(Class<?> clazz,
                                            FieldCallback fc, FieldFilter ff) {
        if(clazz == null || clazz == Object.class){
            return ;
        }

        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            //过滤掉 static字段 和 final字段
            if(ff == null || !ff.matches(field)){
                continue;
            }

            try {
                fc.doWith(field);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("非法访问属性" + field.getName() + ":" + e) ;
            }


        }

    }

    /**
     * 获取给定的clazz中被被annotationClass标识的方法
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static Method[] getDeclaredMethodWithAnnotationPresent(Class clazz, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList<Method>();
        for(Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)){
                methods.add(method);
            }
        }
        return methods.toArray(new Method[0]);
    }

    /**
     * 获得第一个使用指定注解声明的方法
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static Method getFirstDeclaredMethodWithAnnotationPresent(Class clazz, Class<? extends Annotation> annotationClass) {
        for(Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)){
                return method;
            }
        }

        return null;
    }

    /**
     * 获取给定的clazz中被annotationClass标识的属性
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static Field[] getDeclaredFieldWithAnnotationPresent(Class clazz, Class<? extends Annotation> annotationClass) {
        List<Field> fields = new ArrayList<Field>();
        for(Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)){
                fields.add(field);
            }
        }
        return fields.toArray(new Field[0]);
    }

    /**
     * 获得第一个使用指定注解声明的属性
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static Field getFirstDeclaredFieldWithAnnotationPresent(Class clazz, Class<? extends Annotation> annotationClass) {
        for(Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)){
                return field;
            }
        }

        return null;
    }
}
