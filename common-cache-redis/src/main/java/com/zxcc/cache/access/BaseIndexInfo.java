package com.zxcc.cache.access;

import com.zxcc.cache.annotation.Index;
import com.zxcc.cache.annotation.PrimaryKey;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuanzh.cc on 2016/7/23.
 */
public abstract class BaseIndexInfo<K extends Serializable, T> implements IndexInfo<K, T> {
    private static final Logger logger = LoggerFactory.getLogger(IndexInfo.class);

    /** 唯一索引字段 */
    protected Field primaryKeyField;

    /** 索引信息  索引名字 -> 索引字段 */
    protected Map<String, Field> indexFieldMap;

    protected Map<String, Method> indexMethodMap;

    /** 实体类型 */
    protected Class<T> entityClass;

    public BaseIndexInfo(Class entityClass){
        this.entityClass = entityClass;
        this.resolveEntityClass();
    }

    /**
     * 获取索引对应的名称
     * @param indexName
     * @return
     */
    protected abstract String getIndexName(String indexName);

    @Override
    public K getPrimaryKey(T entity) {
        try {
            return (K) this.primaryKeyField.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 解析 实体类
     */
    protected void resolveEntityClass() {
        ReflectionUtils.doWithFields(this.entityClass, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                field.setAccessible(true);
                if(field.isAnnotationPresent(PrimaryKey.class)) {
                    BaseIndexInfo.this.primaryKeyField = field;
                } else if (field.isAnnotationPresent(Index.class)) {
                    BaseIndexInfo.this.putFieldIndex(field);
                }
            }
        }, new ReflectionUtils.FieldFilter() {
            @Override
            public boolean matches(Field field) {
                return field.isAnnotationPresent(Index.class) || field.isAnnotationPresent(PrimaryKey.class);
            }
        });

        ReflectionUtils.doWithMethods(this.entityClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                BaseIndexInfo.this.putMethodIndex(method);
            }
        }, new ReflectionUtils.MethodFilter() {
            @Override
            public boolean matches(Method method) {
                return method.isAnnotationPresent(Index.class);
            }
        });
    }

    /**
     * 是否有重复的索引
     * @param indexName 索引名称
     * @return
     */
    private boolean hasRepeatIndex(String indexName){
        return (BaseIndexInfo.this.indexFieldMap != null && BaseIndexInfo.this.indexFieldMap.containsKey(indexName)) ||
                (BaseIndexInfo.this.indexMethodMap != null && BaseIndexInfo.this.indexMethodMap.containsKey(indexName));
    }

    /**
     * 将索引字段放入添加到索引字段 map
     * @param indexField
     */
    private void putFieldIndex(Field indexField){
        Index index = indexField.getAnnotation(Index.class);
        String indexName = index.name();
        if(StringUtils.isBlank(indexName)) {
            indexName = BaseIndexInfo.this.getIndexName(indexField.getName());
        }

        //判断是否重复
        if(this.hasRepeatIndex(indexName)) {
            FormattingTuple message = MessageFormatter.format("类型{}的索引名称{}重复!", entityClass.getName(), indexName);
            throw new IllegalStateException(message.getMessage());
        }

        if(BaseIndexInfo.this.indexFieldMap == null) {
            BaseIndexInfo.this.indexFieldMap = new HashMap<String, Field>();
        }
        this.indexFieldMap.put(indexName, indexField);
    }

    /**
     * 将索引方法添加到索引方法 map
     * @param indexMethod
     */
    private void putMethodIndex(Method indexMethod) {
        Index index = indexMethod.getAnnotation(Index.class);
        String indexName = index.name();
        if(StringUtils.isBlank(indexName)) {
            indexName = BaseIndexInfo.this.getIndexName(indexMethod.getName());
        }

        //判断是否重复
        if(this.hasRepeatIndex(indexName)) {
            FormattingTuple message = MessageFormatter.format("类型{}的索引名称{}重复!", entityClass.getName(), indexName);
            throw new IllegalStateException(message.getMessage());
        }

        if(BaseIndexInfo.this.indexMethodMap == null) {
            BaseIndexInfo.this.indexMethodMap = new HashMap<String, Method>();
        }
        this.indexMethodMap.put(indexName, indexMethod);
    }

    @Override
    public Map<String, Field> getAllIndexField() {
        return this.indexFieldMap;
    }

    @Override
    public Map<String, Method> getAllIndexMethod() {
        return this.indexMethodMap;
    }
}
