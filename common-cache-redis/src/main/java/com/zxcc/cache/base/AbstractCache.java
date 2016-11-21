package com.zxcc.cache.base;

import com.zxcc.cache.access.AccessFactory;
import com.zxcc.cache.access.Accessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 缓存基类
 * Created by xuanzh.cc on 2016/7/23.
 */
public abstract class AbstractCache<K extends Serializable, T> implements Cache<K, T> {

    protected Accessor<K, T> accessor;

    @Autowired
    @Qualifier("redisAccessFactory")
    private AccessFactory accessFactory;

    @PostConstruct
    public void init(){
        Class<T> entityClass = this.getEntityClass();
        if(entityClass == null) {
            throw new IllegalStateException();
        }
        this.accessor = accessFactory.getAccessor(entityClass);
    }

    /**
     * 获取实体class
     * @return
     */
    protected Class<T> getEntityClass(){
        Type type = this.getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType)type).getActualTypeArguments();
            if(parameterizedType.length < 2) {
                return null;
            }
            return (Class<T>) parameterizedType[1];
        }
        return null;
    }

    @Override
    public T obtain(K key) {
        return this.load(key, false);
    }

    @Override
    public K save(T entity) {
        //保存到数据库，
        this.saveToDb(entity);
        //保存到缓存
        return this.accessor.save(entity);
    }

    protected T load(K key, boolean needCreate) {
        //缓存获取
        T entity = this.accessor.get(key);
        //缓存不存在
        if(entity == null) {
            //数据库获取
            entity = this.getFromDbByPrimaryKey(key);
            //数据库不存在
            if(entity == null) {
                //如果需要创建
                if(needCreate) {
                    //创建实体
                    entity = this.create(key);
                    //保存到数据库和缓存
                    this.save(entity);
                    return entity;
                } else {
                    return null;
                }
            } else {
                //保存到缓存
                this.accessor.save(entity);
                return entity;
            }
        } else {
            return entity;
        }
    }

    /**
     * 保存到数据库
     * @param entity
     * @return
     */
    public abstract int saveToDb(T entity);

    /**
     * 根据主键从数据库获取
     * @param key
     * @return
     */
    public abstract T getFromDbByPrimaryKey(K key);

    /**
     * 创建 实体
     * @param key
     * @return
     */
    public abstract T create(K key);

    /**
     * 存到缓存
     * @param entity
     */
    public void saveToCache(T entity) {
        this.accessor.save(entity);
    }

    public List<T> loadByKey(String indexName, String indexValue) {
        return this.accessor.getByIndex(indexName, indexValue);
    }
}
