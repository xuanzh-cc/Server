package com.zxcc.studyTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.beans.PropertyDescriptor;

public class MyInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
{

	public MyInstantiationAwareBeanPostProcessor()
	{
		super();
		System.out.println("这是InstantiationAwareBeanPostProcessorAdapter实现类的构造器");
	}
	
	//接口方法，实例化bean之前调用
	@Override
	public Object postProcessBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
		System.out.println("这是InstantiationAwareBeanPostProcessorAdapter调用postProcessBeforeInstantiation方法， 此时对象[" + beanName + "]还没有初始化");
		return null;
	}
	
	//接口方法，实例化之后调用
	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		System.out.println("这是InstantiationAwareBeanPostProcessorAdapter调用postProcessAfterInstantiation方法，此时对象[" + beanName + "]已经实例化");
		return true;
	}
	
	//接口方法，设置某个属性的时候调用
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
		System.out.println("这是InstabtiationAwareBeanPostProcessorAdapter调用postProcessPropertyValues方法， 此时对象[" + beanName + "] 还没有设置用户指定的属性值");
		return pvs;
	}
	
}
