package com.zxcc.socket.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zxcc.socket.model.Config;
import com.zxcc.socket.core.ChannelHandlerInitializer;


public final class WlApp {
	private static final Logger logger = LoggerFactory.getLogger(WlApp.class);
    static final int PORT = Integer.parseInt(System.getProperty("port", "33333"));


    public static void main(String[] args) throws Exception {

        ClassPathXmlApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
			
		}
		
		catch (Exception e) {
			logger.error("初始化服务器应用上下文出错:{}", e.getMessage(), e);
			Runtime.getRuntime().exit(-1);
		}
		ctx.registerShutdownHook();
		ctx.start();

		ChannelHandlerInitializer channelHandlerInit = ctx.getBean(ChannelHandlerInitializer.class);
		Config config = ctx.getBean(Config.class);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(channelHandlerInit);

            
            b.bind(config.getAddress().getPort()).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
        
        logger.error("服务器已经启动");

		while (ctx.isActive() ) {
			
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				if (logger.isDebugEnabled()) {
					logger.debug("服务器主线程被非法打断", e);
				}
			}
		}
		ctx.stop();
		System.out.println("服务器已经关闭");
		while (ctx.isRunning()) {
			Thread.yield();
		}
		System.out.println("服务器已经关闭 - main");
		logger.error("服务器已经关闭 ");
		Runtime.getRuntime().exit(0);
    }
}
