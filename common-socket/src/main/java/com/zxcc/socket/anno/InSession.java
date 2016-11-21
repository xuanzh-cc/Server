package com.zxcc.socket.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface InSession
{
	/**
	 * key 字符串
	 * @return
	 */
	String value();
	
	/**
	 * 是否要求参数不能为空
	 * @return
	 */
	boolean required() default true;
}
