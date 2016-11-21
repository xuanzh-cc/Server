package com.zxcc.socket.model;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 服务端常用配置信息
 * @author LJJ
 *
 */
@Component
public class Config
{
	@Value("${service.serverId}")
	/** 服务器标识(主键生成) */
	private int serverId;
	
	/** 运营商标识(主键生成) */
	@Value("${server.config.operator}")
	private short operator;
	
	/**
	 * 服务器ip
	 */
	@Value("${service.ip}")
	private String ip;
	/**
	 * 服务器端口
	 */
	@Value("${service.port}")
	private int port;

	public int getServerId()
	{
		return serverId;
	}

	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}

	public InetSocketAddress getAddress()
	{
		InetSocketAddress address = new InetSocketAddress(ip, port);
		return address;
	}

	public short getOperator()
	{
		return operator;
	}

	public void setOperator(short operator)
	{
		this.operator = operator;
	}

	
	
	
	
	
	
}
