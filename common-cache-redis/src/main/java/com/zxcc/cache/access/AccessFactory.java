package com.zxcc.cache.access;

/**
 * Created by xuanzh.cc on 2016/7/23.
 */
public interface AccessFactory {

    /**
     * 获取数据访问接口
     * @param clazz
     */
    public Accessor getAccessor(Class<?> clazz);



}
