package com.zxcc.resources.reader;

import com.zxcc.resources.constant.ResourcesType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 资源读取器容器
 * Created by xuanzh.cc on 2016/6/29.
 */
public class ReaderHolder implements ApplicationContextAware{
    private ApplicationContext applicationContext;

    private ConcurrentHashMap<ResourcesType, ResourceReader> readerMap = new ConcurrentHashMap<ResourcesType, ResourceReader>();

    @PostConstruct
    public void init(){
        Map<String,ResourceReader> readers = this.applicationContext.getBeansOfType(ResourceReader.class);
        for(String beanName : readers.keySet()){
            ResourceReader reader = readers.get(beanName);
            readerMap.put(reader.getResourceType(), reader);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 根据资源类型获取对应的资源读取器
     * @param resourcesType
     * @return
     */
    public ResourceReader getReader(ResourcesType resourcesType) {
        return this.readerMap.get(resourcesType);
    }
}
