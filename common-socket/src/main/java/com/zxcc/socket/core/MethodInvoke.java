package com.zxcc.socket.core;

import io.netty.channel.Channel;

import com.zxcc.game.protobuf.ModuleProtocol.RequestMsg;
import com.zxcc.socket.model.Result;
/***
 * 执行器
 * @author LJJ
 *
 */
public interface MethodInvoke {
	
	/**
	 * 执行方法，返回结果
	 * @param request
	 * @param channel 
	 * @return
	 */
	public Result invoke(RequestMsg request, Channel channel);

}
