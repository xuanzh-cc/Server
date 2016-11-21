package com.zxcc.cache.access.redis;

import com.zxcc.cache.access.BaseIndexInfo;

import java.io.Serializable;

/**
 * Created by xuanzh.cc on 2016/7/23.
 */
public class RedisIndexInfo<K extends Serializable, T> extends BaseIndexInfo<K, T> {

    /** 索引分隔串 */
    private static final String SEPARATOR = ":" ;
    /** 主键标识 */
    private static final String PRIMARY_KEY = "PK" ;
    /** 索引标识 */
    private static final String INDEX = "INDEX";

    /** 主键类型的 map的key名称， 格式为 mapKeyName -> <K, T>*/
    private String primaryKeyMapKeyName;
    /** 索引前缀 */
    private String indexKeyPrefix;

    public RedisIndexInfo(Class entityClass){
        super(entityClass);
        this.primaryKeyMapKeyName = entityClass.getName() + SEPARATOR + PRIMARY_KEY;
        this.indexKeyPrefix = entityClass.getName() + SEPARATOR + INDEX + SEPARATOR;
    }

    @Override
    protected String getIndexName(String indexName) {
        return this.indexKeyPrefix + indexName;
    }

    @Override
    public String getIndexValue(String indexName, String indexValue) {
        return this.getIndexName(indexName) + SEPARATOR + indexValue;
    }

    public String getPrimaryKeyMapKeyName() {
        return primaryKeyMapKeyName;
    }
}
