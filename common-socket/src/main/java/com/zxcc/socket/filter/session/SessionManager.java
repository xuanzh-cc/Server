package com.zxcc.socket.filter.session;

import com.zxcc.game.protobuf.ModuleProtocol.ResponseMsg;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.util.Collection;
import java.util.List;

/**
 * 会话管理器接口
 * 
 * @author LJJ
 */
public interface SessionManager {

	/** 用户身份标识键 */
	String IDENTITY = "identity";
	/** 身份处理完成标记键 */
	String PROCEED = "proceed";
	/** 忽略事件通知的标记键 */
	String IGNORE_EVENT = "ignore_event";

//	/** 用户身份标识会话属性 */
//	AttributeKey<Object> ATT_IDENTITY = AttributeKey.valueOf(IDENTITY);
	/** 身份处理完成标记的会话属性 */
	AttributeKey<Boolean> ATT_PROCEED = AttributeKey.valueOf(PROCEED);
	/** 忽略事件通知的会话属性 */
	AttributeKey<Boolean> ATT_IGNORE_EVENT = AttributeKey.valueOf(IGNORE_EVENT);

	/**
	 * 向指定目标发送请求
	 * 
	 * @param request
	 * @param ids
	 */
	void send(ResponseMsg request, Object... ids);

	/**
	 * 向指定目标发送请求
	 * 
	 * @param request
	 * @param sessions
	 */
	void send(ResponseMsg request, Channel... sessions);

	/**
	 * 向所有会话发送请求
	 * 
	 * @param request
	 */
	void sendAll(ResponseMsg request);

	/**
	 * 向所有已经验证身份的会话发送请求
	 * 
	 * @param request
	 */
	void sendAllIdentified(ResponseMsg request);

	/**
	 * 向所有匿名会话发送请求
	 * 
	 * @param request
	 */
	void sendAllAnonymous(ResponseMsg request);

	/**
	 * 检查指定用户是否在线
	 * 
	 * @param ids
	 *            用户标识
	 * @return
	 */
	boolean isOnline(Object... ids);

	/**
	 * 添加会话监听器，每一种事件类型的监听器只能添加一个
	 * 
	 * @param listener
	 *            监听器实例
	 * @throws IllegalStateException
	 *             重复添加同一类型的监听器时抛出
	 */
//	void addListener(SessionListener listener);

	/**
	 * 踢指定的用户下线
	 * 
	 * @param cause
	 *            原因标识{@link SessionEventCause}
	 * @param ids
	 *            被踢下线的用户标识
	 */
	Collection<?> kick(int cause, Object... ids);

	/**
	 * 将全部的在线用户踢下线
	 * 
	 * @param cause
	 *            原因标识{@link SessionEventCause}
	 */
	Collection<?> kickAll(int cause);

	/**
	 * 获取全部的在线用户标识
	 * 
	 * @return
	 */
	Collection<?> getOnlineIdentities();

	/**
	 * 获取指定用户标识的会话对象
	 * 
	 * @param id
	 *            用户标识
	 * @return 会返回null，被延时关闭的会话也会返回
	 */
	Channel getSession(Object id);

	/**
	 * 获取全部的匿名会话
	 * 
	 * @return
	 */
	Collection<Channel> getAnonymous();

	/**
	 * 复制会话属性
	 * 
	 * @param src
	 *            来源对象
	 * @param dest
	 *            复制目的地
	 */
	void copyAttributes(Channel src, Channel dest);

	/**
	 * 统计当前的会话数量
	 * 
	 * @param includeAnonymous
	 *            是否包含匿名会话
	 * @return
	 */
	int count(boolean includeAnonymous);

	void putIdentity(Object identity, Channel channel);

	void cleanCache(long owner);

	int getMaxCSn(long owner);

	List<ResponseMsg> pushNextsMessage(long owner, int ssn);

	
	
//	/**
//	 * 删除缓存中的数据
//	 * @param owner 玩家名字
//	 */
//	void cleanCache(long owner);
//	
//
//	/**
//	 * 获取接受到玩家的最大sn
//	 * @param owner 玩家id
//	 * @return
//	 */
//	long getMaxSn(long owner);
}
