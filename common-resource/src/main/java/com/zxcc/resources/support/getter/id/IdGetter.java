package com.zxcc.resources.support.getter.id;

/**
 * 唯一标识获取接口
 * Created by xuanzh.cc on 2016/6/29.
 */
public interface IdGetter {

    /**
     * 获取资源对象唯一标识值
     * @param object
     * @return
     */
    Object getValue(Object object);
}
