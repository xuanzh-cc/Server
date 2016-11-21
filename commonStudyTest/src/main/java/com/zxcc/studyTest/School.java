package com.zxcc.studyTest;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class School implements Serializable
{
	private static final long serialVersionUID = 7675387319473545592L;
	private int age;

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}
	
}
