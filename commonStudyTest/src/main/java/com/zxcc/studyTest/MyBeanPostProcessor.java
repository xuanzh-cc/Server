package com.zxcc.studyTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor
{
	public MyBeanPostProcessor()
	{
		super();
		System.out.println("这是BeanPostProcessor实现类的构造器");
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
	{
		System.out.println("BeanPostProcessor接口方法postProcessBeforeInitialization对 " + beanName + " 的属性进行修改");
		return bean;
	}
	
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
	{
		System.out.println("BeanPostProcessor接口方法postProcessAfterInitialization对 " + beanName + " 属性进行修改");
		return bean;
	}

}
