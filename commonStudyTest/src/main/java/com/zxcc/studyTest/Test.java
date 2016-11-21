package com.zxcc.studyTest;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


public class Test
{
	public static void main(String args[]) throws InstantiationException, IllegalAccessException, IOException, ClassNotFoundException{
		System.out.println("测试开始");
		
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		System.out.println("初始化完毕");
		
		//获取 person 实例
		
		Person person = ctx.getBean("person", Person.class);
		System.out.println(person);
		
		Student  student = ctx.getBean("student", Student.class);

		Teacher  teacher = ctx.getBean("teacher", Teacher.class);

		System.out.println("注册关闭钩子");
		((ClassPathXmlApplicationContext)ctx).registerShutdownHook();
		
		//ctx.refresh();
	}
	
}
