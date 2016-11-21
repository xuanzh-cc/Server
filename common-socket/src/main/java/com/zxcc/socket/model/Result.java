package com.zxcc.socket.model;

import com.google.protobuf.ByteString;

public class Result
{

	private int code;
	private ByteString body;
	public int getCode()
	{
		return code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
	public ByteString getBody()
	{
		return body;
	}
	public void setBody(ByteString body)
	{
		this.body = body;
	}	
}
