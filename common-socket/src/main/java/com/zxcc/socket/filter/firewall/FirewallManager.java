package com.zxcc.socket.filter.firewall;

import io.netty.channel.Channel;

import java.util.Collection;

/**
 * 防火墙管理接口
 * 
 * @author LJJ
 */
public interface FirewallManager {

	/**
	 * 获取当前的黑名单列表
	 * 
	 * @return
	 */
	Collection<String> getBlockList();

	/**
	 * 获取当前的白名单列表
	 * 
	 * @return
	 */
	Collection<String> getAllowList();

	/**
	 * 阻止指定IP的连接
	 * 
	 * @param ip
	 */
	void block(String ip);

	/**
	 * 阻止指定会话的IP连接
	 * 
	 * @param channel
	 *            被阻止的会话
	 * @return 被阻止的IP
	 */
	String blockByIoSession(Channel channel);

	/**
	 * 解除对指定IP连接的阻止
	 * 
	 * @param ip
	 */
	void unblock(String ip);

	/**
	 * 开启阻止全部非白名单IP的连接的状态
	 */
	void blockAll();

	/**
	 * 关闭阻止全部非白名单IP的连接的状态
	 */
	void unblockAll();

	/**
	 * 添加指定的白名单IP
	 * 
	 * @param ip
	 */
	void allow(String ip);

	/**
	 * 移除指定的白名单IP
	 * 
	 * @param ip
	 */
	void disallow(String ip);

	/**
	 * 获取当前的连接数
	 * 
	 * @return
	 */
	int getCurrentConnections();
}
