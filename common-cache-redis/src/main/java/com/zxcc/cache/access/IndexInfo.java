package com.zxcc.cache.access;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 实体索引信息
 * Created by xuanzh.cc on 2016/7/23.
 */
public interface IndexInfo<K extends Serializable, T> {

    /**
     * 获取实体主键
     * @param entity
     * @return
     */
    K getPrimaryKey(T entity);

    /**
     * 获取实体类型
     * @return
     */
    Class<T> getEntityClass();

    /**
     * 获取索引的值
     * @param indexName
     * @param indexValue
     * @return
     */
    String getIndexValue(String indexName, String indexValue);

    /**
     * 获取所有的索引字段
     * @return
     */
    Map<String, Field> getAllIndexField();

    /**
     * 获取所有的索引方法
     * @return
     */
    Map<String, Method> getAllIndexMethod();
}
