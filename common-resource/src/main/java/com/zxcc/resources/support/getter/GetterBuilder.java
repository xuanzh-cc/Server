package com.zxcc.resources.support.getter;

import com.zxcc.resources.annotation.Id;
import com.zxcc.resources.annotation.Index;
import com.zxcc.resources.support.getter.id.FieldIdGetter;
import com.zxcc.resources.support.getter.id.IdGetter;
import com.zxcc.resources.support.getter.id.MethodIdGetter;
import com.zxcc.resources.support.getter.index.FieldIndexGetter;
import com.zxcc.resources.support.getter.index.IndexGetter;
import com.zxcc.resources.support.getter.index.MethodIndexGetter;
import com.zxcc.utility.ReflectionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuanzh.cc on 2016/6/29.
 */
public class GetterBuilder {
    private static final Logger logger = LoggerFactory.getLogger(GetterBuilder.class);

    /**
     * 创建指定资源的唯一标识获取器
     * @param clazz
     * @return
     */
    public static IdGetter createIdGetter(Class clazz) {
        IdentityInfo info = new IdentityInfo(clazz);
        IdGetter identifier = null;
        if(info.isFiled()) {
            identifier = new FieldIdGetter(info.field);
        } else {
            identifier = new MethodIdGetter(info.method);
        }
        return identifier;
    }

    /**
     * 创建指定资源的索引获取器
     * @param clazz
     * @return
     */
    public static Map<String,IndexGetter> createIndexGetters(Class<?> clazz) {
        Map<String,IndexGetter> result = new HashMap<String,IndexGetter>();
        Field[] fields = ReflectionUtility.getDeclaredFieldWithAnnotationPresent(clazz, Index.class);
        Method[] methods = ReflectionUtility.getDeclaredMethodWithAnnotationPresent(clazz, Index.class);

        List<IndexGetter> indexGetters = new ArrayList<IndexGetter>(fields.length + methods.length);

        for (Field field : fields) {
            IndexGetter indexGetter = new FieldIndexGetter(field);
            indexGetters.add(indexGetter);
        }
        for (Method method : methods){
            IndexGetter indexGetter = new MethodIndexGetter(method);
            indexGetters.add(indexGetter);
        }

        for (IndexGetter indexGetter : indexGetters) {
            String indexName = indexGetter.getIndexName();
            if(result.containsKey(indexName)){
                FormattingTuple message = MessageFormatter.format("资源类[{}]的索引名[{}]重复",
                        clazz, indexName);
                logger.error(message.getMessage());
                throw new RuntimeException(message.getMessage());
            }
            result.put(indexGetter.getIndexName(), indexGetter);
        }
        return result;
    }

    /**
     * 识别信息
     */
    private static class IdentityInfo {
        public Field field;
        public Method method;

        public IdentityInfo(Class clazz) {
            Field[] fields = ReflectionUtility.getDeclaredFieldWithAnnotationPresent(clazz, Id.class);
            //唯一标识个数大于一个
            if(fields.length > 1){
                FormattingTuple message = MessageFormatter.format("资源类[{}]的属性唯一标识声明重复", clazz);
                logger.error(message.getMessage());
                throw new RuntimeException(message.getMessage());
            }

            if (fields.length == 1){
                this.field = fields[0];
                this.method = null;
                return;
            }

            Method[] methods = ReflectionUtility.getDeclaredMethodWithAnnotationPresent(clazz, Id.class);
            //唯一标识个数大于一个
            if(methods.length > 1){
                FormattingTuple message = MessageFormatter.format("资源类[{}]的属性唯一标识声明重复", clazz);
                logger.error(message.getMessage());
                throw new RuntimeException(message.getMessage());
            }

            if (methods.length == 1){
                this.field = null;
                this.method = methods[0];
                return;
            }

            //唯一标识不存在
            FormattingTuple message = MessageFormatter.format("资源类[{}]缺少唯一标识声明", clazz);
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage());
        }

        /**
         * 是否使用属性来作为唯一标识 或 索引
         * @return
         */
        public boolean isFiled(){
            return this.field != null;
        }

        /**
         * 判断是否使用方法来作为唯一标识 或 索引
         * @return
         */
        public boolean isMethod(){
            return this.method != null;
        }
    }

}
