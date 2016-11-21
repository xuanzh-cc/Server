package com.zxcc.socket.core;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Sharable
public class FilterTest extends ChannelInboundHandlerAdapter implements ChannelOutboundHandler{

	private Logger logger = LoggerFactory.getLogger(FilterTest.class);
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		logger.debug("add");
//		System.out.println(IpUtils.getIp(ctx.channel()));
		super.handlerAdded(ctx);
		
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		logger.debug("removed");
		super.handlerRemoved(ctx);
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.debug("exceptionCaught");
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelRegistered");
		super.channelRegistered(ctx);
		
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelUnregistered");
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelActive");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive");
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		logger.debug("channelRead");
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelReadComplete");
		super.channelReadComplete(ctx);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		logger.debug("userEventTriggered");
		super.userEventTriggered(ctx, evt);
		
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		logger.debug("channelWritabilityChanged");
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress,
			ChannelPromise promise) throws Exception {
		logger.debug("bind");
		ctx.bind( localAddress, promise);
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
			SocketAddress localAddress, ChannelPromise promise)
			throws Exception {
		logger.debug("connect");
		ctx.connect( remoteAddress, localAddress, promise);
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		logger.debug("disconnect");
		ctx.disconnect( promise);
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		logger.debug("close");
		ctx.close( promise);
	}

//	@Override
//	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise)
//			throws Exception {
//		System.out.println("deregister");
//		super.deregister(ctx, promise);
//	}

	@Override
	public void read(ChannelHandlerContext ctx) throws Exception {
		logger.debug("read");
		ctx.read();
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
			ChannelPromise promise) throws Exception {
		logger.debug("write,promise");
		ctx.write( msg, promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.debug("flush");
		ctx.flush();
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
	{
		
		ctx.deregister(promise);
	}

}
