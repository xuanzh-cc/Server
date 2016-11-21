package com.zxcc.cache.access;

import java.io.Serializable;
import java.util.List;

/**
 * 数据访问接口
 * K 键
 * T 值
 * Created by xuanzh.cc on 2016/7/23.
 */
public interface Accessor<K extends Serializable, T> {

    /**
     * 保存实体到缓存
     * @param entity
     * @return
     */
    K save(T entity);

    /**
     * 根据 key 获取实体信息
     * @param key
     * @return
     */
    T get(K key);

    /**
     * 更新 实体信息
     * @param entity
     */
    void update(T entity);

    /**
     * 根据 key 移除实体信息
     * @param key
     * @return
     */
    boolean remove(K key);

    /**
     * 移除实体信息
     * @param entity
     * @return
     */
    boolean remove(T entity);

    List<T> getByIndex(String indexName, String indexValue);
}
