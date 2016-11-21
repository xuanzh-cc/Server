/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.zxcc.socket.core;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zxcc.game.protobuf.ModuleProtocol.RequestMsg;
import com.zxcc.socket.exception.ResultException;

@Sharable
@Component
public class SupportHandler extends SimpleChannelInboundHandler<RequestMsg>
{

	/** 当前的通信控制器 */
	@Autowired
	private HandlerDispatch handler;

	private static final Logger logger = LoggerFactory.getLogger(SupportHandler.class);
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestMsg msg) throws Exception
	{
		
		handler.addMessageQueue(ctx, msg);
	}  
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
	{
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		
		logger.error("IO异常 cause : " + cause.getMessage());
		
		if(cause instanceof ResultException)
		{
			
		}
		else
		{
//			logger.error("IO exceptionCaught异常 关闭客服端连接");
			ctx.close();
		}
		
	}


	
	
	 @Override  
	    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)  
	            throws Exception {  
	        if (evt instanceof IdleStateEvent) {  
	            IdleStateEvent e = (IdleStateEvent) evt;  
	            if(e.state() == IdleState.ALL_IDLE)
	            {
	            	logger.debug("ALL_IDLE 超时"); 
	            	ctx.close();  
	            }
	            else if (e.state() == IdleState.READER_IDLE) {  
	                
	                	logger.debug("READER_IDLE 读超时");  
	            } else if (e.state() == IdleState.WRITER_IDLE) {  
	          
	            	logger.debug("WRITER_IDLE 写超时");  
	            }  
	        }  
	    }


	
}
