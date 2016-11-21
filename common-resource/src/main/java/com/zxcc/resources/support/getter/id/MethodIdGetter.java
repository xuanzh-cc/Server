package com.zxcc.resources.support.getter.id;

import com.zxcc.utility.ReflectionUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 唯一标识获取器（基于方法）
 * Created by xuanzh.cc on 2016/6/30.
 */
public class MethodIdGetter implements IdGetter {

    private static final Logger logger = LoggerFactory.getLogger(MethodIdGetter.class);

    private Method method;

    public MethodIdGetter(Method method) {
        ReflectionUtility.makeAccessible(method);
        this.method = method;
    }

    @Override
    public Object getValue(Object value) {
        try {
            return method.invoke(value);
        } catch (Exception e) {
            FormattingTuple message = MessageFormatter.format("资源类标识符方法访问异常", e);
            logger.error(message.getMessage());
            throw new RuntimeException(message.getMessage(), e);
        }
    }
}
