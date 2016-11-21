package com.zxcc.cache.access;

import com.zxcc.cache.annotation.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by xuanzh.cc on 2016/7/23.
 */
public abstract class AbstractAccessorFactory implements AccessFactory, ApplicationContextAware{

    protected static final Logger logger = LoggerFactory.getLogger(AbstractAccessorFactory.class);

    protected Set<String> entityPackages;

    private ApplicationContext applicationContext;

    /** 默认资源匹配符 */
    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    /** 资源搜索分析器 */
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    /** 类的元数据读取器，由它来负责读取类上的注释信息 */
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    protected Map<Class<?>, Accessor> accessorMap = new HashMap<Class<?>, Accessor>();

    /**
     * 加载实体信息
     */
    public void loadEntityInfo(){
        try {
            List<Resource> resources = this.findResources(this.entityPackages.toArray(new String[0]));
             for (Resource resource : resources) {
                 MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                if (!annotationMetadata.hasAnnotation(Entity.class.getName())) {
                    continue; // 不是要处理的资源，忽略
                }

                ClassMetadata clzMeta = metadataReader.getClassMetadata();
                Class<?> clazz = this.applicationContext.getClassLoader().loadClass(clzMeta.getClassName());

                 if(clazz.isAnnotationPresent(Entity.class)) {
                     accessorMap.put(clazz, this.createAccessor(clazz));
                 }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建对应实现的 accessor
     * @param clazz
     * @return
     */
    protected abstract Accessor createAccessor(Class clazz);

    /**
     * 资源搜索
     * @param packages
     * @return
     */
    protected List<Resource> findResources(String... packages) {
        if (packages == null || packages.length == 0) {
            return Collections.EMPTY_LIST;
        }

        List<Resource> resources = new ArrayList<Resource>();
        for (String name : packages) {
            String resPath = ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils
                    .resolvePlaceholders(name));
            String path = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resPath + "/"
                    + DEFAULT_RESOURCE_PATTERN;
            Resource[] tmp = null;
            try {
                tmp = resourcePatternResolver.getResources(path);
            }
            catch (IOException e) {
                throw new IllegalStateException("无法读取包[" + name + "]内的资源");
            }
            Collections.addAll(resources, tmp);
        }
        return resources;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Set<String> getEntityPackages() {
        return entityPackages;
    }

    public void setEntityPackages(Set<String> entityPackages) {
        this.entityPackages = entityPackages;
    }

    /**
     * 获取对应类型的数据访问接口
     * @param clazz
     * @return
     */
    @Override
    public Accessor getAccessor(Class<?> clazz) {
        return this.accessorMap.get(clazz);
    }
}
