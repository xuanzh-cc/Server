package com.zxcc.cache.access.redis;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuanzh.cc on 2016/7/23.
 */
public class SerializableUtils {
    private static ConcurrentHashMap<Class, RuntimeSchema> runtimeSchemaMap = new ConcurrentHashMap<Class, RuntimeSchema>();

    /**
     * 获取 RuntimeSchema
     * @param clazz
     * @return
     */
    private static RuntimeSchema getRuntimeSchema(Class clazz){
        RuntimeSchema runtimeSchema = runtimeSchemaMap.get(clazz);
        if(runtimeSchema == null) {
            runtimeSchema = RuntimeSchema.createFrom(clazz);
            RuntimeSchema old = runtimeSchemaMap.putIfAbsent(clazz, runtimeSchema);
            if( old == null) {
                return runtimeSchema;
            }
            return old;
        }

        return runtimeSchema;
    }

    /**
     * 序列化
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T entity) {
        RuntimeSchema<T> runtimeSchema = getRuntimeSchema(entity.getClass());
        byte[] bytes = ProtostuffIOUtil.toByteArray(entity, runtimeSchema,
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        return bytes;
    }


    /**
     * 反序列化
     * @param entityClass
     * @param bytes
     * @param <T>
     * @return
     */
    public static <T> T unserialize(Class<T> entityClass, byte[] bytes) {
        RuntimeSchema<T> runtimeSchema = getRuntimeSchema(entityClass);
        //空对象
        T result = runtimeSchema.newMessage();
        //反序列化
        ProtostuffIOUtil.mergeFrom(bytes, result, runtimeSchema);
        return result;
    }
}
