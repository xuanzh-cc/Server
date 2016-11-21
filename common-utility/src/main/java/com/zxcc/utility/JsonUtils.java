package com.zxcc.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * JSON 工具类， Map 的key 只可以为简单类型，不能为复杂类型
 * Created by xuanzh.cc on 2016/6/29.
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    private JsonUtils(){
        throw new IllegalAccessError("该类不可以实例化...");
    }

    /**
     * 将对象转换为字符串
     * @param object
     * @return
     */
    public static String object2String(Object object){
        StringWriter writer = new StringWriter();

        try {
            mapper.writeValue(writer, object);
        } catch (IOException e) {
            logger.error("将 object 转换为 json串时发生异常", e);
            return null;
        }
        return writer.toString();
    }
}
