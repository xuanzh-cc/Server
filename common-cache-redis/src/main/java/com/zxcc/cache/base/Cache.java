package com.zxcc.cache.base;

import java.io.Serializable;

/**
 * Created by xuanzh.cc on 2016/7/23.
 */
public interface Cache<K extends Serializable, T> {

    /**
     * 获取数据，不存在则返回空
     * @param key
     * @return
     */
    public T obtain(K key);

    /**
     * 保存实体
     * @param entity
     * @return
     */
    public K save(T entity);
}
