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
package com.zxcc.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxcc.game.protobuf.ModuleProtocol.ResponseMsg;

public class ClientHandler extends SimpleChannelInboundHandler<ResponseMsg>
{

	// Stateful properties
//	private volatile Channel channel;
	private final ConcurrentHashMap<String,LinkedBlockingQueue<ResponseMsg>> answer = new ConcurrentHashMap<String,LinkedBlockingQueue<ResponseMsg>>();
	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	public ClientHandler()
	{
		super(true);
	}

	public ResponseMsg getResult(int cmd , int mod)
	{
		String key = cmd + "_" + mod;
		ResponseMsg msg;
		boolean interrupted = false;
		for (;;)
		{
			try
			{
				LinkedBlockingQueue<ResponseMsg> tmp = answer.get(key);
				if(tmp == null)
				{
					tmp = new LinkedBlockingQueue<>();
					LinkedBlockingQueue<ResponseMsg> pre = answer.put(key, tmp);
					if(pre != null)
					{
						tmp = pre;
					}
				}
				msg = tmp.take();
				logger.debug(msg.toString());
				break;
			}
			catch (InterruptedException ignore)
			{
				interrupted  = true;
			}
		}

		if (interrupted)
		{
			Thread.currentThread().interrupt();
		}
		return msg;
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx)
	{
//		channel = ctx.channel();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, ResponseMsg msg)
	{
//		System.out.println(msg);
		String key = msg.getHeader().getCmd() + "_" + msg.getHeader().getMod();
		LinkedBlockingQueue<ResponseMsg> tmp = answer.get(key);
		if(tmp == null)
		{
			tmp = new LinkedBlockingQueue<>();
			LinkedBlockingQueue<ResponseMsg> pre = answer.put(key, tmp);
			if(pre != null)
			{
				tmp = pre;
			}
		}
		tmp.add(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		cause.printStackTrace();
		ctx.close();
	}

	
}
