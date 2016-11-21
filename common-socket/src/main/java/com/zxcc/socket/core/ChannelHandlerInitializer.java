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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zxcc.game.protobuf.ModuleProtocol.RequestMsg;
import com.zxcc.socket.filter.firewall.FirewallFilter;
import com.zxcc.socket.filter.session.SessionManagerFilter;
import com.zxcc.socket.protobuf.ProtobufDecoder;
import com.zxcc.socket.protobuf.ProtobufEncoder;
import com.zxcc.socket.protobuf.ProtobufVarint32FrameDecoder;
import com.zxcc.socket.protobuf.ProtobufVarint32LengthFieldPrepender;

@Component
public class ChannelHandlerInitializer extends ChannelInitializer<SocketChannel>
{

	@Autowired
	private SupportHandler supportHandler;
	@Autowired
	private FirewallFilter firewallFilter;
	@Autowired
	private SessionManagerFilter sessionManagerFilter;
	@Value("${server.heart.timeout}")
	private int heartTimeout = 15;

	private static ProtobufDecoder protobufDecoder = new ProtobufDecoder(RequestMsg.getDefaultInstance());

	private static ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender = new ProtobufVarint32LengthFieldPrepender();

	private static ProtobufEncoder protobufEncoder = new ProtobufEncoder();
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline p = ch.pipeline();

		p.addLast(firewallFilter);
		p.addLast(sessionManagerFilter);
		p.addLast(new ProtobufVarint32FrameDecoder());
		p.addLast(protobufDecoder);
		
		p.addLast(protobufVarint32LengthFieldPrepender);
		p.addLast(protobufEncoder);
		p.addLast("idleStateHandler", new IdleStateHandler(heartTimeout, heartTimeout, heartTimeout)); 
		p.addLast(supportHandler);
		
	}
}
