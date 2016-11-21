package com.zxcc.resources;

import com.zxcc.resources.annotation.Resources;
import com.zxcc.resources.support.definition.FormatDefinition;
import com.zxcc.resources.support.definition.ResourceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 资源管理器工厂bean
 * Created by xuanzh.cc on 2016/6/27.
 */
public class StorageManagerFactoryBean implements FactoryBean<StorageManager>, ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(StorageManagerFactoryBean.class);

    // 默认资源匹配符
    protected static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private ApplicationContext applicationContext;

    //资源搜索分析器
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    //类的元数据读取器，由它来读取类上注解信息
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

    //资源类扫描包路径
    private Set<String> packages;

    @Autowired
    private FormatDefinition formatDefinition;

    private ManagedList<ResourceDefinition> definitions = new ManagedList<ResourceDefinition>();

    @PostConstruct
    public void init(){
        if(!CollectionUtils.isEmpty(packages)){
            for (String packageName : this.packages){
                String[] classNames = this.getResource(packageName);

                for(String className : classNames){
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        FormattingTuple message = MessageFormatter.format("无法获取的资源类[{}]", className);
                        logger.error(message.getMessage(), e);
                        throw new RuntimeException(message.getMessage(), e);
                    }
                    definitions.add(new ResourceDefinition(clazz, formatDefinition));
                }
            }
        }
    }

    /**
     * 获取到指定包下的被 Resources 注解标识的 class 全限定名
     * @param packageName， 包名， 可以包含 ${} 占位符， 路径分隔符 /
     * @return
     */
    private String[] getResource(String packageName) {
        try {
            //替换 ${xx.vv} 占位符， 没有${} 的话，则返回原来的字符串
            packageName = SystemPropertyUtils.resolvePlaceholders(packageName);
            // 将 / 路径分隔符替换为 . 分隔符，（即把基于/的资源替换为 类的全限定名）
            String packagePath = ClassUtils.convertResourcePathToClassName(packageName);
            //资源搜索路径
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packageName + "/" + DEFAULT_RESOURCE_PATTERN;

            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);

            //资源注解的名称
            String resourcesAnnotationName = Resources.class.getName();
            Set<String> result = new HashSet<String>();
            for (Resource resource : resources) {
                if(!resource.isReadable()){
                    continue;
                }

                MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                //没有被 Resources 注解标识
                if(!annotationMetadata.hasAnnotation(resourcesAnnotationName)) {
                    continue;
                }

                ClassMetadata classMetadata = metadataReader.getClassMetadata();
                result.add(classMetadata.getClassName());
            }

            return result.toArray(new String[0]);
        } catch (IOException e) {
            String message = "无法读取资源类信息";
            logger.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    public static final void main(String[] args){
        String packagePath = ClassUtils.convertResourcePathToClassName("com/zxcc/reader.ResourceReader");
        String[] result = new StorageManagerFactoryBean().getResource("com/zxcc/reader");
        System.out.println(result[0]);
        System.out.println(result[1]);
    }


    @Override
    public StorageManager getObject() throws Exception {
        StorageManager storageManager = this.applicationContext.getAutowireCapableBeanFactory()
                .createBean(StorageManager.class);
        for (ResourceDefinition definition : this.definitions ) {
            storageManager.initialize(definition);
        }
        return storageManager;
    }

    @Override
    public Class<?> getObjectType() {
        return StorageManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
