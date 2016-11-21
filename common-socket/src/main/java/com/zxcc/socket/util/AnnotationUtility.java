package com.zxcc.socket.util;

import java.lang.annotation.Annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

public class AnnotationUtility extends AnnotationUtils {
	public static Class<?> findAnnotationDeclaringClassAndInterface(
			Class<? extends Annotation> annotationType, Class<? extends Object> clazz) {
		Assert.notNull(annotationType, "Annotation type must not be null");
		if (clazz == null || clazz.equals(Object.class)) {
			return null;
		}
		if (isAnnotationDeclaredLocally(annotationType, clazz)) {
			return clazz;
		}
		for (Class<?> clz : clazz.getInterfaces()) {
			if (isAnnotationDeclaredLocally(annotationType, clz)) {
				return clz;
			}
		}
		return findAnnotationDeclaringClassAndInterface(annotationType, clazz.getSuperclass());
	}
}
