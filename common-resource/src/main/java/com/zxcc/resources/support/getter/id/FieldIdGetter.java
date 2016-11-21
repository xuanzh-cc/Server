package com.zxcc.resources.support.getter.id;

import com.zxcc.utility.ReflectionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.Field;

/**
 * 唯一标识获取器（基于属性）
 * Created by xuanzh.cc on 2016/6/30.
 */
public class FieldIdGetter implements IdGetter {
    private static final Logger logger = LoggerFactory.getLogger(FieldIdGetter.class);
    private Field field;

    public FieldIdGetter(Field field) {
        ReflectionUtility.makeAccessible(field);
        this.field = field;
    }

    @Override
    public Object getValue(Object object) {
        try {
            return this.field.get(object);
        } catch (IllegalAccessException e) {
            FormattingTuple message = MessageFormatter.format("资源类标识符属性访问异常", e);
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage(), e);
        }
    }
}
