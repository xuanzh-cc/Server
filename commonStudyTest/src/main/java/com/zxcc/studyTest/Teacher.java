package com.zxcc.studyTest;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Teacher implements Serializable
{
	private static final long serialVersionUID = -6117770974113366765L;
	private int age;
	private String name;
	private Student student;
	public int getAge()
	{
		return age;
	}
	public void setAge(int age)
	{
		this.age = age;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Student getStudent()
	{
		return student;
	}
	public void setStudent(Student student)
	{
		this.student = student;
	}
}
