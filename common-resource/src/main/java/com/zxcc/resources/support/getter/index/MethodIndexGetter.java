package com.zxcc.resources.support.getter.index;

import com.zxcc.resources.annotation.Index;
import com.zxcc.resources.support.getter.id.MethodIdGetter;
import com.zxcc.utility.ReflectionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 * 索引值获取器（基于方法）
 * Created by xuanzh.cc on 2016/6/30.
 */
public class MethodIndexGetter extends MethodIdGetter implements IndexGetter {

    private static final Logger logger = LoggerFactory.getLogger(MethodIndexGetter.class);

    private final String name;
    private final boolean unique;
    private final Comparator comparator;

    public MethodIndexGetter(Method method) {
        super(method);
        Index index = method.getAnnotation(Index.class);
        this.name = index.name();
        this.unique = index.unique();

        Class<Comparator> comparatorClass = (Class<Comparator>) index.comparator();
        if(!comparatorClass.equals(Comparator.class)){
            try {
                this.comparator = comparatorClass.newInstance();
            } catch (Exception e) {
                FormattingTuple message = MessageFormatter.format("索引比较器[{}]无法被实例化", comparatorClass.getName());
                logger.error(message.getMessage());
                throw new IllegalArgumentException(message.getMessage(), e);
            }
        } else {
            this.comparator = null;
        }
    }

    @Override
    public String getIndexName() {
        return this.name;
    }

    @Override
    public boolean isUnique() {
        return this.unique;
    }

    @Override
    public boolean hasComparator() {
        return this.comparator != null;
    }

    @Override
    public Comparator getComparator() {
        return this.comparator;
    }
}
