package com.zxcc.studyTest;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Student implements Serializable
{
	private static final long serialVersionUID = -1001210314386377364L;
	private int age;
	private String name;
	private int sex;
	public int getAge()
	{
		return age;
	}
	public String getName()
	{
		return name;
	}
	public int getSex()
	{
		return sex;
	}
	public void setAge(int age)
	{
		this.age = age;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setSex(int sex)
	{
		this.sex = sex;
	}
}
