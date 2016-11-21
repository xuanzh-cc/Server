package com.zxcc.studyTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
	

	public MyBeanFactoryPostProcessor()
	{
		super();
		System.out.println("这是 BeanFactoryPostProcessor 实现类构造器");
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
	{
		System.out.println("BeanFactoryPostProcessor调用postProcessBeanFactory方法, 改造beanFactory： " + beanFactory);
		BeanDefinition bd = beanFactory.getBeanDefinition("person");
		bd.getPropertyValues().addPropertyValue("phone", "");
	}

}
