package com.zxcc.studyTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Person implements BeanFactoryAware, BeanNameAware, InitializingBean, DisposableBean, ApplicationContextAware
{
	private String name;
	private String address;
	private String phone;
	
	private BeanFactory beanFactory;

	private String beanName;

	private ApplicationContext ctx;

	public Person() {
		super();
		System.out.println("【构造器】调用person的构造器实例化..");
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name) {
		System.out.println("【设置属性】设置属性name");
		this.name = name;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address) {
		System.out.println("【设置属性】设置属性address");
		this.address = address;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone) {
		System.out.println("【设置属性】设置属性phone");
		this.phone = phone;
	}
	
	//这里是 BeanFactoryAware 接口方法
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("【BeanFactoryAware接口】调用BeanFactoryAware.setBeanFactory()，beanFactory：" + beanFactory);
		this.beanFactory = beanFactory;
	}
	
	//这里是 BeanNameAware 接口方法
	public void setBeanName(String name)
	{
		System.out.println("【BeanNameAware接口】调用BeanNameAware.setBeanName(), name：" + name);
		this.beanName = name;
	}

	//这里是 DisposableBean 接口方法
	public void destroy() throws Exception {
		System.out.println("【DisposableBean接口】调用DisposableBean.destroy()");
	}

	//这里是 InitializingBean 接口方法
	public void afterPropertiesSet() throws Exception {
		System.out.println("【InitializingBean接口】调用InitializingBean.afterPropertiesSet()");
	}

	//通过bean的init-method属性指定初始化的方法
	public void myInit(){
		System.out.println("【init-method】调用bean的init-method属性指定的方法");
	}
	
	//通过destory-method 属性指定的销毁方法
	public void myDestroy(){
		System.out.println("【destroy-method】调用bean的destroy-method属性指定的方法");
	}

	@Override
	public String toString()
	{
		return "Person [name=" + name + ", address=" + address + ", phone=" + phone + "]";
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("【ApplicationContextAware接口】调用ApplicationContextAware.setApplicationContext(), applicationContext：" + applicationContext);
		this.ctx = applicationContext;
	}
}
