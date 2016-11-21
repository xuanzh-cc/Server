package com.zxcc.resources.reader;

import com.zxcc.resources.constant.ResourcesType;

import java.io.InputStream;
import java.util.Iterator;

/**
 * JSON 格式资源读取类
 * Created by xuanzh.cc on 2016/6/27.
 */
public class JsonResourceReader implements ResourceReader {

    public ResourcesType getResourceType() {
        return ResourcesType.JSON;
    }

    public <E> Iterator<E> read(InputStream ins, Class<E> clazz) {
        return null;
    }
}
