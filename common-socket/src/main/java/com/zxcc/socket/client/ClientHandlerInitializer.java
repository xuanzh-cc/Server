package com.zxcc.socket.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import com.zxcc.game.protobuf.ModuleProtocol.ResponseMsg;
import com.zxcc.socket.core.FilterTest;
import com.zxcc.socket.protobuf.ProtobufDecoder;
import com.zxcc.socket.protobuf.ProtobufEncoder;
import com.zxcc.socket.protobuf.ProtobufVarint32FrameDecoder;
import com.zxcc.socket.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ClientHandlerInitializer extends ChannelInitializer<SocketChannel>
{

	// private final SslContext sslCtx;
	//
	// public ClientHandlerInitializer(SslContext sslCtx) {
	// this.sslCtx = sslCtx;
	// }

	private ClientHandler clientHandler;
	private static ProtobufDecoder protobufDecoder = new ProtobufDecoder(ResponseMsg.getDefaultInstance());

	private static ProtobufVarint32LengthFieldPrepender protobufVarint32LengthFieldPrepender = new ProtobufVarint32LengthFieldPrepender();

	private static ProtobufEncoder protobufEncoder = new ProtobufEncoder();

	public ClientHandlerInitializer(ClientHandler clientHandler) throws Exception
	{

		this.clientHandler = clientHandler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception
	{
		ChannelPipeline p = ch.pipeline();
		// if (sslCtx != null) {
		// p.addLast(sslCtx.newHandler(ch.alloc(), Client.HOST, Client.PORT));
		// }
		 p.addLast(new FilterTest());
		p.addLast(new ProtobufVarint32FrameDecoder());
		p.addLast(protobufDecoder);

		p.addLast(protobufVarint32LengthFieldPrepender);
		p.addLast(protobufEncoder);

		p.addLast(clientHandler);
	}

}
