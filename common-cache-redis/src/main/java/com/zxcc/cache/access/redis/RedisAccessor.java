package com.zxcc.cache.access.redis;

import com.zxcc.cache.access.Accessor;
import com.zxcc.cache.access.IndexInfo;
import com.zxcc.cache.annotation.Entity;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuanzh.cc on 2016/7/23.
 */
public class RedisAccessor<K extends Serializable, T> implements Accessor<K, T> {

    private ConfiguredJedisPool configuredJedisPool;

    /** 线程上下文相关的 jedis，在一个线程内，共享一个 jedis  */
    private IndexInfo<K, T> indexInfo;

    public static ThreadLocal<Jedis> jedisThreadLocal = new ThreadLocal<Jedis>();

    public RedisAccessor(IndexInfo<K, T> indexInfo, ConfiguredJedisPool configuredJedisPool) {
        this.indexInfo = indexInfo;
        this.configuredJedisPool = configuredJedisPool;
    }

    /**
     * 获取 jedis
     * @return
     */
    public Jedis obtainJedisResource() {
        Jedis jedisResource = RedisAccessor.jedisThreadLocal.get();
        if(jedisResource == null) {
            jedisResource = this.configuredJedisPool.getResource();
            this.jedisThreadLocal.set(jedisResource);
        }
        return jedisResource;
    }

    @Override
    public K save(T entity) {
        Jedis jedis = this.obtainJedisResource();
        //Transaction transaction = jedis.multi();
        K primaryKey = this.putPrimaryEntity(jedis, entity);
        this.putIndexEntityIds(jedis, primaryKey, entity);
        //transaction.exec();
        return primaryKey;
    }

    /**
     * 将主键相关的实体添加到redis  pk -> entity
     * @param jedis
     * @param entity
     * @return
     */
    private K putPrimaryEntity(Jedis jedis, T entity){
        K primaryKey = this.indexInfo.getPrimaryKey(entity);
        String primaryKeyValue = ((RedisIndexInfo)this.indexInfo).getPrimaryKeyMapKeyName();
        jedis.hset(this.getStrByte(primaryKeyValue),this.getStrByte(primaryKey.toString()), SerializableUtils.serialize(entity));
        return primaryKey;
    }

    private byte[] getStrByte(String str) {
        return str.getBytes(Charset.forName("utf-8"));
    }

    /**
     * 将索引相关的 主键值 添加到 redis   index -> Set<pk>
     * @param jedis
     * @param key
     * @param entity
     */
    private void putIndexEntityIds(Jedis jedis, K key, T entity){
        Map<String, Field> allIndexField = this.indexInfo.getAllIndexField();
        if(allIndexField == null ) {
            return ;
        }
        for(String indexName : allIndexField.keySet() ){
            Field indexField = allIndexField.get(indexName);
            try {
                String indexValue = indexField.get(entity).toString();
                String indexKeyValue = this.indexInfo.getIndexValue(indexName, indexValue);
                jedis.sadd(indexKeyValue, key.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Map<String, Method> allMethodField = this.indexInfo.getAllIndexMethod();
        if(allMethodField == null ) {
            return ;
        }
        for(String indexName : allMethodField.keySet() ){
            Method indexMethod = allMethodField.get(indexName);
            try {
                String indexValue = indexMethod.invoke(entity).toString();
                String indexKeyValue = this.indexInfo.getIndexValue(indexName, indexValue);
                jedis.sadd(indexKeyValue, key.toString());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public T get(K key) {
        return this.getByPrimaryKeyStr(key.toString());
    }

    private T getByPrimaryKeyStr(String primaryKeyStr) {
        Jedis jedis = this.obtainJedisResource();
        String primaryKeyValue = ((RedisIndexInfo)this.indexInfo).getPrimaryKeyMapKeyName();
        byte[] bytes = jedis.hget(this.getStrByte(primaryKeyValue), this.getStrByte(primaryKeyStr));
        if(bytes == null) {
            return null;
        }
        return SerializableUtils.unserialize(this.indexInfo.getEntityClass(), bytes);
    }

    @Override
    public void update(T entity) {
        Jedis jedis = this.obtainJedisResource();
    }

    @Override
    public boolean remove(K key) {
        Jedis jedis = this.obtainJedisResource();
        String primaryKeyValue = ((RedisIndexInfo)this.indexInfo).getPrimaryKeyMapKeyName();
        Long delNum = jedis.hdel(this.getStrByte(primaryKeyValue), this.getStrByte(key.toString()));

        //对应的索引也要删除

        return delNum != null && delNum > 0;
    }

    public void removeIndexEntityId(Jedis jedis, T entity) {
        K key = this.indexInfo.getPrimaryKey(entity);
        Map<String, Field> allIndexField = this.indexInfo.getAllIndexField();
        if(allIndexField == null ) {
            return ;
        }
        for(String indexName : allIndexField.keySet() ){
            Field indexField = allIndexField.get(indexName);
            try {
                String indexValue = indexField.get(entity).toString();
                String indexKeyValue = this.indexInfo.getIndexValue(indexName, indexValue);
                jedis.srem(indexKeyValue, key.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Map<String, Method> allMethodField = this.indexInfo.getAllIndexMethod();
        if(allMethodField == null ) {
            return ;
        }
        for(String indexName : allMethodField.keySet() ){
            Method indexMethod = allMethodField.get(indexName);
            try {
                String indexValue = indexMethod.invoke(entity).toString();
                String indexKeyValue = this.indexInfo.getIndexValue(indexName, indexValue);
                jedis.srem(indexKeyValue, key.toString());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean remove(T entity) {
        K key = this.indexInfo.getPrimaryKey(entity);
        return this.remove(key);
    }

    @Override
    public List<T> getByIndex(String indexName, String indexValue) {
        List<T> result = new ArrayList<>();
        String indexKeyValue = this.indexInfo.getIndexValue(indexName, indexValue);
        Jedis jedis = this.obtainJedisResource();
        Set<String> keyStrSet = jedis.smembers(indexKeyValue);
        if(keyStrSet == null) {
            return result;
        }
        for(String keyStr : keyStrSet){
            T entity = this.getByPrimaryKeyStr(keyStr);
            if(entity != null) {
                result.add(entity);
            }
        }
        return result;
    }


/*    @Override
    public T getByIndex(String indexName, String indexValue) {
        String redisIndex = this.indexInfo.getIndex(indexName, indexValue);
        Jedis jedis = this.obtainJedisResource();
        //jedis.
        return (T)null;
    }*/

}
