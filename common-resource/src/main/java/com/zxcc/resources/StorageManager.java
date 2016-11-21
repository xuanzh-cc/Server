package com.zxcc.resources;

import com.zxcc.resources.support.definition.ResourceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源管理器
 * Created by xuanzh.cc on 2016/6/27.
 */
public class StorageManager implements ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(StorageManager.class);

    AutowireCapableBeanFactory beanFactory;

    //资源class -> 资源信息定义
    private ConcurrentHashMap<Class, ResourceDefinition> definitionMap = new ConcurrentHashMap<Class, ResourceDefinition>();
    //资源class -> 资源存储空间
    private ConcurrentHashMap<Class, Storage> storageMap = new ConcurrentHashMap<Class, Storage>();

    /**
     * 初始化静态类资源
     * @param definition  资源定义
     */
    public void initialize(ResourceDefinition definition) {
        Class clazz = definition.getClazz();
        //如果已经存在该类型的资源
        if(definitionMap.putIfAbsent(clazz, definition) != null){
            ResourceDefinition prev = definitionMap.get(clazz);
            FormattingTuple message = MessageFormatter.format("类[{}]的资源定义已经存在", clazz, prev);
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage());
        }

        initializeStorage(clazz);
    }

    /**
     * 初始化类资源的存储空间
     * @param clazz
     */
    private Storage initializeStorage(Class clazz) {
        ResourceDefinition definition = this.definitionMap.get(clazz);

        if(definition == null){
            FormattingTuple message = MessageFormatter.format("静态资源[{}]的信息定义不存在！", clazz);
            logger.error(message.getMessage());
            throw new IllegalStateException(message.getMessage());
        }

        Storage storage = beanFactory.createBean(Storage.class);
        Storage prev = this.storageMap.putIfAbsent(clazz, storage);
        if(prev == null){
            storage.initialize(definition);
        }

        return prev == null ? storage : prev ;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
    }

    /**
     * 获取指定类型的存储空间
     * @param clazz
     * @return
     */
    public Storage getStorage(Class clazz) {
        if(this.storageMap.containsKey(clazz)) {
            return this.storageMap.get(clazz);
        }

        return this.initializeStorage(clazz);
    }
}
