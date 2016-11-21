package com.zxcc.socket.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指令声明注释
 * 
 * @author LJJ
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Command {

	/**
	 * 指令值，默认:0
	 * 
	 * @return
	 */
	byte value() default 0;

	/**
	 * 指令所属模块声明，可选
	 * 
	 * @return
	 */
	byte modules() default 0;

	/**
	 * 标识消息体是否采用压缩
	 * 
	 * @return
	 */
	Compress compress() default @Compress();

	
}
