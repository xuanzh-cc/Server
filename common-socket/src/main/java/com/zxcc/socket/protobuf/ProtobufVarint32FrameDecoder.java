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
package com.zxcc.socket.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

import com.google.protobuf.CodedInputStream;

/**
 * A decoder that splits the received {@link ByteBuf}s dynamically by the value
 * of the Google Protocol Buffers <a href=
 * "http://code.google.com/apis/protocolbuffers/docs/encoding.html#varints">Base
 * 128 Varints</a> integer length field in the message. For example:
 * 
 * <pre>
 * BEFORE DECODE (302 bytes)       AFTER DECODE (300 bytes)
 * +--------+---------------+      +---------------+
 * | Length | Protobuf Data |----->| Protobuf Data |
 * | 0xAC02 |  (300 bytes)  |      |  (300 bytes)  |
 * +--------+---------------+      +---------------+
 * </pre>
 * 
 * @see CodedInputStream
 */
public class ProtobufVarint32FrameDecoder extends ByteToMessageDecoder
{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
	{
		in.markReaderIndex();
		int bufLenght = 0;
		if (!in.isReadable())
		{
			in.resetReaderIndex();
			return;
		}
		
		if (in.readableBytes() < 4)
		{
			in.resetReaderIndex();
			return;
		}

		bufLenght = in.readInt();
		if (bufLenght < 0)
		{
			throw new CorruptedFrameException("negative length: " + bufLenght);
		}

		if (in.readableBytes() < bufLenght)
		{
			in.resetReaderIndex();
			return;
		}
		else
		{
			out.add(in.readBytes(bufLenght));
			return;
		}
	}
}
