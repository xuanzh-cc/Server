package com.zxcc.resources;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import com.zxcc.resources.annotation.Index;
import com.zxcc.resources.reader.ReaderHolder;
import com.zxcc.resources.reader.ResourceReader;
import com.zxcc.resources.support.definition.InjectDefinition;
import com.zxcc.resources.support.definition.ResourceDefinition;
import com.zxcc.resources.support.getter.GetterBuilder;
import com.zxcc.resources.support.getter.id.IdGetter;
import com.zxcc.resources.support.getter.index.IndexGetter;
import com.zxcc.utility.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 资源存储空间对象
 * Created by xuanzh.cc on 2016/6/27.
 */
public class Storage<K, V> extends Observable implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(Storage.class);

    //spring容器
    private ApplicationContext applicationContext;

    //资源定义信息
    private ResourceDefinition definition;

    //资源读取器容器
    @Autowired
    private ReaderHolder readerHolder;

    //资源读取器
    private ResourceReader reader;

    //唯一标识获取器
    private IdGetter idGetter;
    //索引获取器map
    private Map<String, IndexGetter> indexGetters;

    //是否已经初始化的标记
    private volatile boolean isInitialized;

    //资源数据主存储空间map
    private Map<K, V> values = new HashMap<K, V>();
    //索引存储空间  索引 -> 索引对应的值
    private Map<String, Map<Object, List<V>>> indexs = new HashMap<String, Map<Object, List<V>>>();
    //唯一值存储空间  唯一索引 -> 唯一值
    private Map<String, Map<Object, V>> uniques = new HashMap<String, Map<Object, V>>();

    //读锁
    private Lock readLock;
    //写锁
    private Lock writeLock;

    /**
     * 构造方法
     */
    public Storage() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    /**
     * 初始化方法，只运行一次
     * @param definition
     */
    public void initialize(ResourceDefinition definition) {
        //已经初始化了
        if(isInitialized){
            return ;  //避免重复初始化
        }

        //设置标记为已初始化
        this.isInitialized = true;
        this.definition = definition;
        this.reader = readerHolder.getReader(definition.getResourcesType());
        this.idGetter = GetterBuilder.createIdGetter(definition.getClazz());
        this.indexGetters = GetterBuilder.createIndexGetters(definition.getClazz());

        //注入静态属性
        Set<InjectDefinition> staticInjects = definition.getStaticInjects();
        for(InjectDefinition staticInject : staticInjects) {
            Field field = staticInject.getField();
            Object injectValue = staticInject.getValue(this.applicationContext);
            try {
                field.set(null, injectValue);
            } catch (IllegalAccessException e) {
                FormattingTuple message = MessageFormatter.arrayFormat("无法注入值[{}]到资源[{}]静态属性[{}]中", new Object[]{injectValue, this.getClazz().getName(), field.getName()});
                logger.error(message.getMessage());
                throw new IllegalStateException(message.getMessage(), e);
            }
        }

        //加载资源到存储空间
        this.reload();
    }

    /**
     * 获取指定唯一标识对应的资源实例
     * @param key  唯一标识
     * @param flag 不能存在时，是否抛出异常  true:抛出异常   false:不抛出异常
     * @return
     */
    public V get(K key, boolean flag) {
        this.isReady();
        this.readLock.lock();
        try {
            V result = this.values.get(key);
            if(flag && result == null) {
                FormattingTuple message = MessageFormatter.format("标识为[{}]的静态资源[{}]不存在", key ,
                        this.getClazz().getName());
                logger.error(message.getMessage());
                throw new IllegalStateException(message.getMessage());
            }
            return result;
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * 判断是否包含指定的唯一标识
     * @param key
     * @return
     */
    public boolean containsId(K key){
        this.isReady();
        this.readLock.lock();
        try {
            return this.values.containsKey(key);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * 返回全部的资源实例
     * @return 返回的集合是只读的，无法进行元素的添加或者移除
     */
    public Collection<V> getAll() {
        this.isReady();
        this.readLock.lock();

        try {
            return Collections.unmodifiableCollection(this.values.values());
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * 获取指定的唯一索引实例
     * @param indexName 索引名称
     * @param indexValue 索引值
     * @return 唯一索引对应的资源实例， 不存在则返回null
     */
    public V getUnique(String indexName, Object indexValue) {
        this.isReady();
        this.readLock.lock();
        try {
            Map<Object, V> index = this.uniques.get(indexName);
            if( index == null ) {
                return null;
            }
            return index.get(indexValue);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * 获取指定索引名称的内容列表
     * @param indexName 索引名称
     * @param indexValue 索引值
     * @return 不存在会返回{@link Collections#EMPTY_LIST}
     */
    public List<V> getIndex(String indexName, Object indexValue) {
        this.isReady();
        this.readLock.lock();
        try {
            Map<Object, List<V>> index = this.indexs.get(indexName);
            if(index == null) {
                return Collections.EMPTY_LIST;
            }
            List<V> indexList = index.get(indexValue);
            if (indexList == null) {
                return Collections.EMPTY_LIST;
            }
            return new ArrayList<>(indexList);
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * 重新加载资源到存储空间
     */
    private void reload() {
        this.isReady();
        writeLock.lock();
        try {
            //获取资源数据
            Resource resource = applicationContext.getResource(this.getLocation());
            InputStream ins = resource.getInputStream();
            Iterator<V> iterator = this.reader.read(ins, this.getClazz());
            //清理存储空间
            this.clear();
            while(iterator.hasNext()){
                V obj = iterator.next();
                Set<InjectDefinition> fieldInjectDefinitions = this.definition.getInjects();
                for(InjectDefinition injectDefinition : fieldInjectDefinitions) {
                    Field field = injectDefinition.getField();
                    Object value = injectDefinition.getValue(applicationContext);
                    try {
                        field.set(obj, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                if (this.put(obj) != null){
                    FormattingTuple message = MessageFormatter.format("[{}]资源[{}]唯一标识重复",
                            this.getClazz().getName(), JsonUtils.object2String(obj));
                    logger.error(message.getMessage());
                    throw new IllegalStateException(message.getMessage());
                }
            }

            //对需要排序的的索引对应的存储空间进行排序
            for(Map.Entry<String, Map<Object, List<V>>> entry : this.indexs.entrySet()) {
                String indexName = entry.getKey();
                IndexGetter indexGetter = this.indexGetters.get(indexName);
                if(indexGetter.hasComparator()){
                    for(List<V> values : entry.getValue().values()) {
                        Collections.sort(values, indexGetter.getComparator());
                    }
                }
            }

            //通知监听器
            this.setChanged();
            this.notifyObservers();
        } catch (IOException e) {
            FormattingTuple message = MessageFormatter.format("资源[{}]对应的资源文件[{}]不存在",
                    this.getClazz().getName(), this.getLocation());
        }  finally {
            writeLock.unlock();
        }

    }

    /**
     * 判断是否初始化就绪
     *
     * @throws RuntimeException 未初始化时抛出异常
     */
    private void isReady() {
        if(!isInitialized) {
            String message = "未初始化完成";
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    private Object put(V value) {
        K key = (K) this.idGetter.getValue(value);
        if(key == null) {
            FormattingTuple message = MessageFormatter.format("资源类[{}]存在标识属性为null的配置项", getClazz().getName());
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage());
        }
        V result = this.values.put(key, value);

        //索引处理
        for(String indexName : this.indexGetters.keySet()) {
            IndexGetter indexGetter = this.indexGetters.get(indexName);
            //索引值
            Object indexKey = indexGetter.getValue(value);
            if(indexKey == null){
                FormattingTuple message = MessageFormatter.format("资源类[{}]的索引[{}]的值为null",
                        getClazz().getName(), indexName);
                logger.debug(message.getMessage());
                continue;
            }

            //索引内容存储
            //唯一索引
            if(indexGetter.isUnique()) {
                Map<Object, V> indexUniqueValue = this.loadUniqueIndex(indexName);
                if (indexUniqueValue.put(indexKey, value) != null) {
                    FormattingTuple message = MessageFormatter.arrayFormat("资源类[{}]唯一索引（索引名[{}]）的值[{}]重复",
                            new Object[]{this.getClazz().getName(), indexName, indexKey});
                }
            }
            else {
                List<V> indexListValue = this.loadListIndex(indexName, indexKey);
                indexListValue.add(value);
            }
        }

        return result;
    }

    /**
     * 获取非唯一索引值对应的 存储空间
     * @param indexName
     * @param indexKey
     * @return
     */
    private List<V> loadListIndex(String indexName, Object indexKey) {
        if(this.indexs.containsKey(indexName)){
            Map<Object, List<V>> indexValues = this.indexs.get(indexName);
            if(indexValues.containsKey(indexKey)){
                return indexValues.get(indexKey);
            } else {
                List<V> result = new ArrayList<V>();
                indexValues.put(indexKey, result);
                return  result;
            }
        } else {
            Map<Object, List<V>> indexValues = new HashMap<>();
            this.indexs.put(indexName, indexValues);
            List<V> result = new ArrayList<V>();
            indexValues.put(indexKey, result);
            return  result;
        }
    }

    /**
     * 获取唯一索引对应的的存储空间
     * @param indexName
     * @return
     */
    private Map<Object,V> loadUniqueIndex(String indexName) {
        if(this.uniques.containsKey(indexName)) {
            return this.uniques.get(indexName);
        }

        Map<Object, V> result = new HashMap<Object, V>();
        this.uniques.put(indexName, result);
        return result;
    }

    /**
     * 清理全部的存储空间
     */
    private void clear() {
        this.values.clear();
        this.indexs.clear();
        this.uniques.clear();
    }

    /**
     * 获取资源文件对应的class
     * @return
     */
    public Class getClazz(){
        return this.definition.getClazz();
    }

    /**
     * 获取资源文件的路径
     * @return
     */
    private String getLocation(){
        return this.definition.getLocation();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
