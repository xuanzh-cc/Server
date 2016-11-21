package com.zxcc.resources.support.definition;

import com.zxcc.resources.annotation.Inject;
import com.zxcc.resources.annotation.Resources;
import com.zxcc.resources.constant.ResourcesType;
import com.zxcc.utility.ReflectionUtility;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * 资源定义信息
 * Created by xuanzh.cc on 2016/6/27.
 */
public class ResourceDefinition {
    //文件后缀分隔符
    public final static String FILE_SUFFIX_SPLIT = ".";
    //文件路径分隔符
    public final static String FILE_PATH_SEPARATOR = File.separator;

    //资源类
    private final Class<?> clazz;
    //资源路径
    private final String location;
    //资源格式
    private final ResourcesType resourcesType;
    //资源的注入信息
    private final Set<InjectDefinition> intects = new HashSet<InjectDefinition>();

    /**
     * 注入属性过滤器
     */
    private ReflectionUtils.FieldFilter INJECT_FIELD_FILTER = new ReflectionUtils.FieldFilter() {
        @Override
        public boolean matches(Field field) {
            return field.isAnnotationPresent(Inject.class);
        }
    };


    public ResourceDefinition(Class<?> clazz, FormatDefinition formatDefinition) {
        this.clazz = clazz;
        this.resourcesType = formatDefinition.getResourcesType();

        Resources resourcesAnno = clazz.getAnnotation(Resources.class);

        //资源文件名称
        String name = resourcesAnno.value();
        //资源名称没设置，则用类名称
        if(StringUtils.isBlank(name)){
            name = clazz.getSimpleName();
        } else {
            if(StringUtils.startsWith(name, FILE_PATH_SEPARATOR)) {
                name = StringUtils.substringAfter(name, FILE_PATH_SEPARATOR);
            }
        }

        this.location = formatDefinition.getLocation()
                + FILE_PATH_SEPARATOR + name + FILE_SUFFIX_SPLIT
                + formatDefinition.getSuffix();

        ReflectionUtility.doWithDeclaredFields(this.clazz, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                InjectDefinition injectDefinition = new InjectDefinition(field);
                intects.add(injectDefinition);
            }
        }, INJECT_FIELD_FILTER);
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String getLocation() {
        return location;
    }

    public ResourcesType getResourcesType() {
        return resourcesType;
    }

    public Set<InjectDefinition> getIntects() {
        return intects;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * 获取静态属性注入定义
     * @return
     */
    public Set<InjectDefinition> getStaticInjects() {
        Set<InjectDefinition> result = new HashSet<InjectDefinition>();
        for(InjectDefinition injectDefinition : this.intects) {
            Field field = injectDefinition.getField();
            if(Modifier.isStatic(field.getModifiers())){
                result.add(injectDefinition);
            }
        }
        return result;
    }

    /**
     * 获取非静态属性注入定义
     * @return
     */
    public Set<InjectDefinition> getInjects() {
        Set<InjectDefinition> result = new HashSet<InjectDefinition>();
        for(InjectDefinition injectDefinition : this.intects) {
            Field field = injectDefinition.getField();
            if(!Modifier.isStatic(field.getModifiers())){
                result.add(injectDefinition);
            }
        }
        return result;
    }
}
