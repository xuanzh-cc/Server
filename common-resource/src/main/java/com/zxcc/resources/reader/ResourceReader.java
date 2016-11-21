package com.zxcc.resources.reader;

import com.zxcc.resources.constant.ResourcesType;

import java.io.InputStream;
import java.util.Iterator;

/**
 * 资源读取接口
 * Created by xuanzh.cc on 2016/6/27.
 */
public interface ResourceReader {

    /**
     * 获取资源类型
     * @return
     */
    ResourcesType getResourceType();

    /**
     * 读取资源
     * @param ins 资源输入流
     * @param clazz 资源类
     * @param <E>
     * @return 资源iterator
     */
    <E> Iterator<E> read(InputStream ins, Class<E> clazz);
}
