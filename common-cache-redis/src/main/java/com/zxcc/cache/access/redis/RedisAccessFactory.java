package com.zxcc.cache.access.redis;

import com.zxcc.cache.access.AbstractAccessorFactory;
import com.zxcc.cache.access.Accessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * redis 数据访问接口 factory
 * Created by xuanzh.cc on 2016/7/23.
 */
@Component
public class RedisAccessFactory extends AbstractAccessorFactory {

    @Autowired
    private ConfiguredJedisPool configuredJedisPool;

    @PostConstruct
    public void init(){
        //加载所有的实体
        this.loadEntityInfo();
    }

    @Override
    protected Accessor createAccessor(Class clazz) {
        Accessor accessor = new RedisAccessor<>(new RedisIndexInfo<>(clazz), configuredJedisPool);
        return accessor;
    }
}
